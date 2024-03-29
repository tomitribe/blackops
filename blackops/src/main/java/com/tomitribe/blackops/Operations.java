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

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum Operations {
    ;

    public static List<Instance> getInstances(final OperationId id) {
        return Aws.getInstances(Aws.client().describeInstances(new DescribeInstances().withOperationId(id).getRequest()));
    }

    public static void terminateOperation(final OperationId id) {

        final TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest();

        final List<Instance> instances = getInstances(id);
        for (final Instance instance : instances) {
            terminateInstancesRequest.withInstanceIds(instance.getInstanceId());
        }

        Aws.client().terminateInstances(terminateInstancesRequest);
    }

    public static void awaitFulfillment(final PrintStream out) throws TimeoutException {
        final DescribeSpotInstanceRequestsRequest describeSpotInstanceRequestsRequest = new DescribeSpotInstanceRequestsRequest();
        final DescribeSpotInstanceRequestsResult result = Aws.client().describeSpotInstanceRequests(describeSpotInstanceRequestsRequest);

        awaitFulfillment(out, result.getSpotInstanceRequests());
    }

    /**
     * Requests the current status of a set of SpotInstanceRequest ids
     *
     * The wait condition is considered met if none of the spot instances are in the "open" state
     */
    public static void awaitFulfillment(final PrintStream out, final List<SpotInstanceRequest> needed) throws TimeoutException {
        Await.await(out, states -> !states.containsKey("open"), () -> {
            final List<String> requestIds = needed.stream().map(SpotInstanceRequest::getSpotInstanceRequestId).collect(Collectors.toList());

            final DescribeSpotInstanceRequestsRequest request = new DescribeSpotInstanceRequestsRequest();
            request.withSpotInstanceRequestIds(requestIds);

            final DescribeSpotInstanceRequestsResult result = Aws.client().describeSpotInstanceRequests(request);

            return countSpotInstanceStates(result.getSpotInstanceRequests());
        });
    }

    public static Map<String, State> countSpotInstanceStates(final List<SpotInstanceRequest> spotInstanceRequests) {
        return States.count(spotInstanceRequests, SpotInstanceRequest::getState);
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

        return Aws.client().requestSpotInstances(request);
    }

    public static LaunchSpecification getLaunchSpecification(final OperationId id) {

        final DescribeInstancesResult result = Aws.client().describeInstances(new DescribeInstances().withOperationId(id).getRequest());

        for (final Instance instance : Aws.getInstances(result)) {

            final String userData = getUserData(instance);

            final LaunchSpecification launchSpecification = getLaunchSpecification(instance);

            launchSpecification.setUserData(userData);

            return launchSpecification;
        }

        throw new IllegalStateException("Unable to find the launch specification");
    }

    public static LaunchSpecification getLaunchSpecification(Instance instance) {
        final DescribeSpotInstanceRequestsResult result1 = Aws.client().describeSpotInstanceRequests(
                new DescribeSpotInstanceRequestsRequest()
                        .withSpotInstanceRequestIds(instance.getSpotInstanceRequestId())
        );

        for (final SpotInstanceRequest spotInstanceRequest : result1.getSpotInstanceRequests()) {
            return spotInstanceRequest.getLaunchSpecification();
        }
        return null;
    }

    public static SpotInstanceRequest getSpotInstanceRequest(final Instance instance) {
        final DescribeSpotInstanceRequestsResult result1 = Aws.client().describeSpotInstanceRequests(
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

        final DescribeInstanceAttributeResult describeInstanceAttributeResult = Aws.client().describeInstanceAttribute(describeInstanceAttributeRequest);
        return describeInstanceAttributeResult.getInstanceAttribute().getUserData();
    }

    public static String formatStatus(List<Instance> instances, List<SpotInstanceRequest> spotInstanceRequests) {
        final Map<String, State> instanceStates = Aws.countInstanceStates(instances);
        final Map<String, State> spotInstanceRequestStates = Aws.countSpotInstanceRequestStates(spotInstanceRequests);
        return formatStatus(instanceStates, spotInstanceRequestStates);
    }

    public static String formatStatus(Map<String, State> instanceStates, Map<String, State> spotInstanceRequestStates) {
        return String.format("Instances: %s - SpotRequests: %s",
                first(Operations::nonEmpty, States.printStates(instanceStates), "none"),
                first(Operations::nonEmpty, States.printStates(spotInstanceRequestStates), "none")
        );
    }

    public static boolean nonEmpty(String s) {
        return s != null && !"".equals(s);
    }

    public static String first(final Predicate<String> predicate, final String... options) {
        for (final String option : options) {
            if (predicate.test(option)) {
                return option;
            }
        }
        return null;
    }
}
