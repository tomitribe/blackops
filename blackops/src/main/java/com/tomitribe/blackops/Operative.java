/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2014
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 */
package com.tomitribe.blackops;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.LaunchSpecification;
import com.amazonaws.services.ec2.model.RequestSpotInstancesRequest;
import com.amazonaws.services.ec2.model.RequestSpotInstancesResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @version $Revision$ $Date$
 */
public class Operative {

    private final LaunchSpecification specification;
    private final Operation operation;
    private final AmazonEC2 client;
    private final RequestSpotInstancesRequest request;

    public Operative(final String operationName) {
        this(operationName, "AKIAJZ4VDNQFF7757XMQ", "7cMdI//R716nejxxD3eIQCsWaJVZT4upPC2FgbDn");
    }

    public Operative(final String operationName, final String accessKey, final String secretKey) {

        specification = new LaunchSpecification()
                .withInstanceType("m3.medium")
                .withImageId("ami-5bf23530") // Operative 1.0
                .withMonitoringEnabled(false)
                .withKeyName("tomitribe_dev")
                .withSecurityGroups("Ports 60000+10");

        operation = new Operation(operationName, accessKey, secretKey);
        operation.tag("user.name", System.getProperty("user.name"));

        client = new AmazonEC2Client(new BasicAWSCredentials(accessKey, secretKey));

        request = new RequestSpotInstancesRequest()
                .withSpotPrice("0.02")
                .withInstanceCount(1)
                .withType("one-time")
                .withLaunchSpecification(specification)
                .withAvailabilityZoneGroup("us-east-1c");
    }

    public RequestSpotInstancesRequest request() {
        return request;
    }

    public LaunchSpecification specification() {
        return specification;
    }

    public Operation operation() {
        return operation;
    }

    public RequestSpotInstancesResult execute() {
        specification.withUserData(operation.toUserData());
        request.setLaunchSpecification(specification);
        return client.requestSpotInstances(request);
    }

    public class Launch {

        private final CountDownLatch expected;
        private final int instanceCount;

        public Launch() {
            instanceCount = request.getInstanceCount();
            expected = new CountDownLatch(instanceCount);

            final ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

            final ScheduledFuture<?> future = pool.scheduleAtFixedRate(this::check, 3, 3, SECONDS);
        }

        public void check() {
            final DescribeInstancesResult result = client.describeInstances(
                    new DescribeInstances()
                            .withOperationId(operation.getId())
                            .withState(InstanceStateName.Running)
                            .getRequest()
            );
        }
    }
}
