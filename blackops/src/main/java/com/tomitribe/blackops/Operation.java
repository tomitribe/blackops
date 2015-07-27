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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.amazonaws.services.ec2.model.InstanceStateName.Pending;
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
                .withOperationId(id.get())
                .withState(states);

        return Aws.getInstances(ec2.describeInstances(describeInstances.getRequest()));
    }

    public List<String> getInstanceIds(final InstanceStateName... states) {
        return Aws.getInstanceIds(getInstances(states));
    }

    public List<SpotInstanceRequest> getSpotInstanceRequests() {
        final DescribeSpotInstanceRequestsRequest request = new DescribeSpotInstanceRequestsRequest().withFilters(id.asFilter());
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
    public List<String> getPrivateDnsNames(final InstanceStateName... states) {
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
        final int needed = getCapacity() - size;

        if (needed > 0) {
            Operations.expandOperation(id, needed);
        }
    }

    public int getCapacity() {
        final Map<String, State> instanceStates = countInstanceStates();
        final int running = States.get(Running.toString(), instanceStates);
        final int pending = States.get(Pending.toString(), instanceStates);
        final int open = States.get(SpotInstanceState.Open.toString(), countSpotInstanceRequestStates());

        return running + pending + open;
    }

}
