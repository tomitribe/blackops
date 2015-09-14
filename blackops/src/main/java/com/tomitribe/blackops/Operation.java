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

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.CancelSpotInstanceRequestsRequest;
import com.amazonaws.services.ec2.model.CancelSpotInstanceRequestsResult;
import com.amazonaws.services.ec2.model.CancelledSpotInstanceRequest;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsRequest;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import com.amazonaws.services.ec2.model.SpotInstanceState;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import org.tomitribe.util.TimeUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.amazonaws.services.ec2.model.InstanceStateName.Running;

/**
 * This class is intentionally stateless.  All state is in EC2 itself.
 *
 * Adding maps, counters, lists or other items that would require updating
 * this object's fields would break the intended design of this class.
 */
public class Operation {

    private final OperationId id;

    private final AmazonEC2 ec2;

    public Operation(final OperationId id, final AmazonEC2 ec2) {
        this.id = id;
        this.ec2 = ec2;
    }

    public List<Instance> getInstances(final InstanceStateName... states) {
        final DescribeInstances describeInstances = new DescribeInstances()
                .withOperationId(id)
                .withState(states);

        return Aws.getInstances(ec2.describeInstances(describeInstances.getRequest()));
    }

    public List<String> getInstanceIds(final InstanceStateName... states) {
        return Aws.getInstanceIds(getInstances(states));
    }

    public List<SpotInstanceRequest> getSpotInstanceRequests(final SpotInstanceState... states) {
        final DescribeSpotInstanceRequestsRequest request = new DescribeSpotInstanceRequestsRequest().withFilters(id.asFilter());

        if (states.length > 0) {
            request.withFilters(Aws.asFilter(states));
        }

        final DescribeSpotInstanceRequestsResult result = ec2.describeSpotInstanceRequests(request);
        return result.getSpotInstanceRequests();
    }

    public List<String> getSpotInstanceRequestIds() {
        return Aws.getSpotInstanceRequestIds(getSpotInstanceRequests());
    }

    /**
     * Obtains the Public DNS names of the running instances
     *
     * Only running instances have a Public DNS
     */
    public List<String> getPublicDnsNames() {
        return getInstances(Running).stream().map(Instance::getPublicDnsName).collect(Collectors.toList());
    }

    /**
     * Obtains the Private DNS names of the running instances
     *
     * Only running instances have a Private DNS
     */
    public List<String> getPrivateDnsNames() {
        return getInstances(Running).stream().map(Instance::getPrivateDnsName).collect(Collectors.toList());
    }

    public List<Tag> getTags() {
        return getInstances().stream()
                .flatMap(instance -> instance.getTags().stream())
                .distinct()
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .collect(Collectors.toList());
    }

    public Map<String, State> countInstanceStates(final InstanceStateName... states) {
        return Aws.countInstanceStates(getInstances(states));
    }

    public Map<String, State> countSpotInstanceRequestStates() {
        return Aws.countSpotInstanceRequestStates(getSpotInstanceRequests());
    }

    /**
     * TODO: Terminate should cancel spot requests and shutdown instances
     * @return
     */
    public List<InstanceStateChange> terminateInstances() {
        final TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest(getInstanceIds());
        final TerminateInstancesResult result = ec2.terminateInstances(terminateInstancesRequest);
        return result.getTerminatingInstances();
    }

    public List<CancelledSpotInstanceRequest> cancelSpotInstanceRequests() {
        final CancelSpotInstanceRequestsRequest cancelSpotInstanceRequestsRequest = new CancelSpotInstanceRequestsRequest(getSpotInstanceRequestIds());
        final CancelSpotInstanceRequestsResult cancelSpotInstanceRequestsResult = ec2.cancelSpotInstanceRequests(cancelSpotInstanceRequestsRequest);
        return cancelSpotInstanceRequestsResult.getCancelledSpotInstanceRequests();
    }

    public void expandCapacityTo(final int size) {
        final int needed = getAnticipatedCapacity() - size;

        if (needed > 0) {
            Operations.expandOperation(id, needed);
        }
    }

    public int getAnticipatedCapacity() {
        final List<Instance> instances = getInstances();
        final List<SpotInstanceRequest> spotInstanceRequests = getSpotInstanceRequests();

        return getAnticipatedCapacity(instances, spotInstanceRequests);
    }

    private static int getAnticipatedCapacity(List<Instance> instances, List<SpotInstanceRequest> spotInstanceRequests) {
        final List<String> ids = Aws.getInstanceIds(instances);

        // Collect all spot instances that are open or active and have not been fulfilled
        final Pattern spotInstancePattern = Pattern.compile("open|active");
        final List<SpotInstanceRequest> anticipatedSpotRequests = spotInstanceRequests.stream()
                .filter(spotInstanceRequest -> !ids.contains(spotInstanceRequest.getInstanceId()))
                .filter(spotInstanceRequest -> spotInstancePattern.matcher(spotInstanceRequest.getState()).matches())
                .collect(Collectors.toList());

        // Collect all instances that are pending or running
        final Pattern instanceFilter = Pattern.compile("pending|running");
        final List<Instance> anticipatedInstances = instances.stream()
                .filter(instance -> instanceFilter.matcher(instance.getState().getName()).matches())
                .collect(Collectors.toList());

        return anticipatedSpotRequests.size() + anticipatedInstances.size();
    }

    /**
     * Should return the list of Instances that are ready and running
     */
    public List<Instance> awaitCapacity(final int needed) throws TimeoutException {
        return awaitCapacity(needed, 1, TimeUnit.SECONDS, 2, TimeUnit.DAYS, (s) -> {});
    }

    /**
     * Should return the list of Instances that are ready and running
     */
    public List<Instance> awaitCapacity(final int needed,
                                        final long retryInterval, final TimeUnit retryUnit,
                                        final long timeout, final TimeUnit timeoutUnit,
                                        final Consumer<String> consumer) throws TimeoutException {

        final long start = System.currentTimeMillis();

        final Supplier<List<Instance>> stringSupplier = () -> {

            final List<Instance> instances = getInstances();
            final List<SpotInstanceRequest> spotInstanceRequests = getSpotInstanceRequests();

            // Report our findings
            consumer.accept(String.format("%s - %s",
                    Operations.formatStatus(instances, spotInstanceRequests),
                    TimeUtils.hoursAndSeconds(System.currentTimeMillis() - start)
            ));

            { // How many instances are we expecting?
                final int missing = needed - getAnticipatedCapacity(instances, spotInstanceRequests);
                if (missing > 0) {
                    final String msg = String.format("\nMore spot instances must be requested: Order %s more", missing);
                    throw new IllegalStateException(msg);
                }
            }

            // Do we have enough running instances?
            final List<Instance> running = instances.stream()
                    .filter(instance -> "running".equals(instance.getState().getName()))
                    .collect(Collectors.toList());

            return running.size() >= needed ? running : null;
        };

        return Await.check(stringSupplier, 0, retryInterval, retryUnit, timeout, timeoutUnit);
    }
}
