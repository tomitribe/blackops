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
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import com.amazonaws.services.ec2.model.SpotInstanceState;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aws {

    private static final AtomicReference<AmazonEC2> ec2 = new AtomicReference<>(
            EC2ResponseLogger.wrap(
                    new AmazonEC2Client(new BasicAWSCredentials("AKIAIOXCE42MYQIB2XYQ", "jwGIwvEGINy8t4wP2h4kCZiXOOUr/69uWvLyFNTi"))
            )
    );

    private Aws() {
    }

    public static void main(String[] args) {

        final Operation operation = new Operation(new OperationId("a546d12a78d381e7723f3edad41913c9f75c9e8a"), client());

        final Instance next = operation.getInstances().iterator().next();
        Operations.getUserData(next);
//        operation.getSpotInstanceRequests()
//                .forEach(System.out::println);


//        final List<Instance> instances = operation.getInstances(InstanceStateName.Running);
//        instances.forEach(System.out::println);

//        client().describeSpotInstanceRequests();
//        client().describeSpotInstanceRequests(new DescribeSpotInstanceRequestsRequest().withSpotInstanceRequestIds("sir-02emmkv4"));

//        client().createTags(new CreateTagsRequest()
//                .withTags(new com.amazonaws.services.ec2.model.Tag("operation-id", "a546d12a78d381e7723f3edad41913c9f75c9e8a"))
//                .withResources(instances.stream().map(Instance::getSpotInstanceRequestId).collect(Collectors.toList()))
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

    public static List<String> getSpotInstanceRequestIds(List<SpotInstanceRequest> spotInstanceRequests) {
        return spotInstanceRequests.stream().map(SpotInstanceRequest::getSpotInstanceRequestId).collect(Collectors.toList());
    }

    public static List<String> getInstanceIds(List<Instance> instances) {
        return instances.stream().map(Instance::getInstanceId).collect(Collectors.toList());
    }

    public static Map<String, State> countSpotInstanceRequestStates(List<SpotInstanceRequest> spotInstanceRequests) {
        return States.count(spotInstanceRequests, SpotInstanceRequest::getState);
    }

    public static Filter asFilter(InstanceStateName... state) {
        final String[] values = Stream.of(state).map(InstanceStateName::toString).toArray(String[]::new);
        return new Filter("instance-state-name").withValues(values);
    }

    public static Filter asFilter(SpotInstanceState... state) {
        final String[] values = Stream.of(state).map(SpotInstanceState::toString).toArray(String[]::new);
        return new Filter("state").withValues(values);
    }
}
