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
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstanceAttributeRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceAttributeResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsRequest;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.LaunchSpecification;
import com.amazonaws.services.ec2.model.RequestSpotInstancesRequest;
import com.amazonaws.services.ec2.model.RequestSpotInstancesResult;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import org.tomitribe.util.Join;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Operations {
    ;

    static final AmazonEC2Client client = new AmazonEC2Client(new BasicAWSCredentials("AKIAJZ4VDNQFF7757XMQ", "7cMdI//R716nejxxD3eIQCsWaJVZT4upPC2FgbDn"));

    public static List<Instance> getInstances(final OperationId id) {

        final DescribeInstancesResult result = client.describeInstances(new DescribeInstances().withOperationId(id.get()).getRequest());

        return Aws.getInstances(result);
    }

    public static void terminateOperation(final OperationId id) {

        final TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest();

        final List<Instance> instances = getInstances(id);
        for (final Instance instance : instances) {
            terminateInstancesRequest.withInstanceIds(instance.getInstanceId());
        }

        client.terminateInstances(terminateInstancesRequest);
    }


    public static void main(String[] args) {
        final PrintStream out = System.out;


    }

    public static void awaitFulfillment(final PrintStream out) {
        final DescribeSpotInstanceRequestsRequest describeSpotInstanceRequestsRequest = new DescribeSpotInstanceRequestsRequest();
        final DescribeSpotInstanceRequestsResult result = client.describeSpotInstanceRequests(describeSpotInstanceRequestsRequest);

        final List<SpotInstanceRequest> spotInstanceRequests = result.getSpotInstanceRequests();


        awaitFulfillment(spotInstanceRequests, out);
    }

    public static void awaitFulfillment(final List<SpotInstanceRequest> needed, final PrintStream out) {
        Await.check(() -> {

            final List<String> requestIds = needed.stream().map(SpotInstanceRequest::getSpotInstanceRequestId).collect(Collectors.toList());

            final DescribeSpotInstanceRequestsRequest describeSpotInstanceRequestsRequest = new DescribeSpotInstanceRequestsRequest().withSpotInstanceRequestIds(requestIds);
            final DescribeSpotInstanceRequestsResult result = client.describeSpotInstanceRequests(describeSpotInstanceRequestsRequest);

            final Map<String, State> count = countSpotInstanceStates(result.getSpotInstanceRequests());

            out.print("\r" + printStates(count));

            if (count.get("open") == null) {

                return "Done";

            } else {

                return null;
            }
        });
    }

    public static String printStates(Map<String, State> count) {
        return Join.join(", ", count.values());
    }

    public static Map<String, State> countSpotInstanceStates(final List<SpotInstanceRequest> spotInstanceRequests) {
        return State.count(spotInstanceRequests, SpotInstanceRequest::getState);
    }


    public static RequestSpotInstancesResult expandOperation(final OperationId id, final int instanceCount) {

        for (final Instance instance : getInstances(id)) {

            return cloneSpotInstance(instanceCount, instance);
        }

        throw new IllegalStateException("Cannot expand operation " + id.get());
    }

    public static RequestSpotInstancesResult cloneSpotInstance(final int instanceCount, final Instance instance) {
        final String userData = getUserData(instance);

        final SpotInstanceRequest spotInstanceRequest = getSpotInstanceRequest(instance);

        final LaunchSpecification launchSpecification = spotInstanceRequest.getLaunchSpecification()
                .withInstanceType(instance.getInstanceType())
                .withImageId(instance.getImageId())
                .withKeyName(instance.getKeyName())
                .withAllSecurityGroups(instance.getSecurityGroups())
                .withUserData(userData);

        final RequestSpotInstancesRequest request = new RequestSpotInstancesRequest()
                .withSpotPrice(spotInstanceRequest.getSpotPrice())
                .withInstanceCount(instanceCount)
                .withType(spotInstanceRequest.getType())
                .withLaunchSpecification(launchSpecification)
                .withAvailabilityZoneGroup(spotInstanceRequest.getAvailabilityZoneGroup());

        return client.requestSpotInstances(request);
    }

    public static LaunchSpecification getLaunchSpecification(final OperationId id) {

        final DescribeInstancesResult result = client.describeInstances(new DescribeInstances().withOperationId(id.get()).getRequest());

        for (final Instance instance : Aws.getInstances(result)) {

            final String userData = getUserData(instance);

            final LaunchSpecification launchSpecification = getLaunchSpecification(instance);

            launchSpecification.setUserData(userData);

            return launchSpecification;
        }

        throw new IllegalStateException("Unable to find the launch specification");
    }

    public static LaunchSpecification getLaunchSpecification(Instance instance) {
        final DescribeSpotInstanceRequestsResult result1 = client.describeSpotInstanceRequests(
                new DescribeSpotInstanceRequestsRequest()
                        .withSpotInstanceRequestIds(instance.getSpotInstanceRequestId())
        );

        for (final SpotInstanceRequest spotInstanceRequest : result1.getSpotInstanceRequests()) {
            return spotInstanceRequest.getLaunchSpecification();
        }
        return null;
    }

    public static SpotInstanceRequest getSpotInstanceRequest(final Instance instance) {
        final DescribeSpotInstanceRequestsResult result1 = client.describeSpotInstanceRequests(
                new DescribeSpotInstanceRequestsRequest()
                        .withSpotInstanceRequestIds(instance.getSpotInstanceRequestId())
        );

        for (final SpotInstanceRequest spotInstanceRequest : result1.getSpotInstanceRequests()) {
            return spotInstanceRequest;
        }

        return null;
    }

    public static String getUserData(Instance instance) {
        final DescribeInstanceAttributeRequest describeInstanceAttributeRequest = new DescribeInstanceAttributeRequest();
        describeInstanceAttributeRequest.withAttribute("userData");
        describeInstanceAttributeRequest.withInstanceId(instance.getInstanceId());

        final DescribeInstanceAttributeResult describeInstanceAttributeResult = client.describeInstanceAttribute(describeInstanceAttributeRequest);
        return describeInstanceAttributeResult.getInstanceAttribute().getUserData();
    }
}
