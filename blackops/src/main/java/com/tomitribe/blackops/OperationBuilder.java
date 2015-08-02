/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.tomitribe.blackops;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.ec2.model.Tag;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version $Revision$ $Date$
 */
public class OperationBuilder {

    private final LaunchSpecification specification;
    private final UserData userData;
    private final AmazonEC2 client;
    private final RequestSpotInstancesRequest request;

    public OperationBuilder(final String operationName) {
        this(operationName, "AKIAJZ4VDNQFF7757XMQ", "7cMdI//R716nejxxD3eIQCsWaJVZT4upPC2FgbDn", EC2ResponseLogger.wrap(new AmazonEC2Client(new BasicAWSCredentials("AKIAJZ4VDNQFF7757XMQ", "7cMdI//R716nejxxD3eIQCsWaJVZT4upPC2FgbDn"))));
    }

    public OperationBuilder(final String operationName, final String accessKey, final String secretKey, final AmazonEC2 client) {

        specification = new LaunchSpecification()
                .withInstanceType("m3.medium")
                .withImageId("ami-5bf23530") // Operative 1.0
                .withMonitoringEnabled(false)
                .withKeyName("tomitribe_dev")
                .withSecurityGroups("Ports 60000+10");

        userData = new UserData(operationName, accessKey, secretKey);
        userData.tag("user.name", System.getProperty("user.name"));

        this.client = client;

        request = new RequestSpotInstancesRequest()
                .withSpotPrice("0.02")
                .withInstanceCount(1)
                .withType("one-time")
                .withLaunchSpecification(specification)
                .withAvailabilityZoneGroup("us-east-1c")
        ;
    }

    public RequestSpotInstancesRequest request() {
        return request;
    }

    public LaunchSpecification specification() {
        return specification;
    }

    public UserData operation() {
        return userData;
    }

    public Operation build() {
        specification.withUserData(userData.toUserData());
        request.setLaunchSpecification(specification);

        // Request the Spot Instances
        final RequestSpotInstancesResult result = client.requestSpotInstances(request);

        // Tag the Spot Instance requests
        client.createTags(new CreateTagsRequest()
                        .withTags(userData.getOperationId().asTag())
                        .withTags(new Tag("Name", userData.getName()))
                        .withResources(Aws.getSpotInstanceRequestIds(result.getSpotInstanceRequests()))
        );

        return new Operation(userData.getOperationId(), client);
    }

    public Launch execute() {
        specification.withUserData(userData.toUserData());
        request.setLaunchSpecification(specification);
        final RequestSpotInstancesResult spotInstancesResult = client.requestSpotInstances(request);

        // Tag the spot instance requests
        client.createTags(new CreateTagsRequest()
                        .withTags(userData.getOperationId().asTag())
                        .withResources(Aws.getSpotInstanceRequestIds(spotInstancesResult.getSpotInstanceRequests()))
        );

        return new Launch(spotInstancesResult);
    }

    public class Launch {

        private final int instanceCount;
        private final RequestSpotInstancesResult spotInstancesResult;
        private final List<String> spotInstanceRequestIds;

        public Launch(final RequestSpotInstancesResult spotInstancesResult) {
            this.instanceCount = request.getInstanceCount();
            this.spotInstancesResult = spotInstancesResult;
            this.spotInstanceRequestIds = spotInstancesResult
                    .getSpotInstanceRequests()
                    .stream()
                    .map(SpotInstanceRequest::getSpotInstanceRequestId)
                    .collect(Collectors.toList());
        }

        public List<Instance> awaitInstances() {

            return Await.check(() -> {
                final DescribeInstancesResult result = client.describeInstances(
                        new DescribeInstances()
                                .withOperationId(userData.getOperationId())
                                .withState(InstanceStateName.Running)
                                .getRequest()
                );

                final List<Instance> instances1 = Aws.getInstances(result);

                final boolean finished = instances1.size() == instanceCount;

                return finished ? instances1 : null;
            });
        }

        public RequestSpotInstancesResult getSpotInstancesResult() {
            return spotInstancesResult;
        }

        public List<String> getSpotInstanceRequestIds() {
            return spotInstanceRequestIds;
        }

        @Override
        public String toString() {
            return spotInstancesResult.toString();
        }
    }
}
