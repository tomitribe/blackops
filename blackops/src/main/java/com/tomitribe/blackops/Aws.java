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
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsRequest;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import com.amazonaws.services.ec2.model.SpotInstanceState;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Aws {

    private static final AtomicReference<AmazonEC2> ec2 = new AtomicReference<>(
            EC2ResponseLogger.wrap(
                    new AmazonEC2Client(new BasicAWSCredentials("AKIAJZ4VDNQFF7757XMQ", "7cMdI//R716nejxxD3eIQCsWaJVZT4upPC2FgbDn"))
            )
    );

    private Aws() {
    }

    public static void main(String[] args) {
//        client().describeSpotInstanceRequests();
//        client().describeSpotInstanceRequests(new DescribeSpotInstanceRequestsRequest().withSpotInstanceRequestIds("sir-02emmkv4"));

//        client().createTags(new CreateTagsRequest()
//                .withTags(new com.amazonaws.services.ec2.model.Tag("operation-id", "834a27d7cb74cc5e99068bd395bbe9d45a42e871"))
//                .withResources("sir-02emmkv4")
//        );
//        client().describeInstances(new DescribeInstancesRequest());
//        final DescribeInstancesResult instancesResult = client().describeInstances(new DescribeInstances().withTag("operation-id", null).getRequest());
//
//        final TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest();
//
//        final List<Instance> instances = getInstances(instancesResult);
//        for (final Instance instance : instances) {
//            terminateInstancesRequest.withInstanceIds(instance.getInstanceId());
//        }
//        client().terminateInstances(terminateInstancesRequest);

//        client().terminateInstances(new TerminateInstancesRequest().withInstanceIds("i-90b43b79"));
    }
    public static AmazonEC2 client() {
        return ec2.get();
    }

    public static void client(final AmazonEC2 client) {
        ec2.set(client);
    }

    public static List<Instance> getInstances(final DescribeInstancesResult result) {
        return result.getReservations().stream()
                .flatMap(reservation -> reservation.getInstances().stream())
                .sorted((o1, o2) -> o2.getLaunchTime().compareTo(o1.getLaunchTime()))
                .collect(Collectors.toList());
    }

    public static DescribeInstancesRequest describeRunningInstances(final List<Tag> tag) {
        return new DescribeInstances()
                .withState(InstanceStateName.Running)
                .withTags(tag)
                .getRequest();
    }


    public static String getName(final Instance instance) {
        for (final com.amazonaws.services.ec2.model.Tag tag : instance.getTags()) {
            if ("Name".equals(tag.getKey())) return tag.getValue();
        }
        return null;
    }

    public static List<SpotInstanceState> getSpotInstanceStates(final DescribeSpotInstanceRequestsResult result) {
        return getSpotInstanceStates(result.getSpotInstanceRequests());
    }

    public static List<SpotInstanceState> getSpotInstanceStates(final List<SpotInstanceRequest> spotInstanceRequests) {
        return spotInstanceRequests.stream()
                .map(spotInstanceRequest -> SpotInstanceState.fromValue(spotInstanceRequest.getState()))
                .collect(Collectors.toList());
    }

    public static Map<String, State> countInstanceStates(List<Instance> instances) {
        return States.count(instances, instance -> instance.getState().getName());
    }
}
