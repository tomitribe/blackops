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
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsRequest;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import com.amazonaws.services.ec2.model.Tag;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Instance> getInstances() {
        final DescribeInstances describeInstances = new DescribeInstances().withOperationId(id.get());
        return Aws.getInstances(ec2.describeInstances(describeInstances.getRequest()));
    }

    public List<String> getInstanceIds() {
        return Aws.getInstanceIds(getInstances());
    }

    public List<SpotInstanceRequest> getSpotInstanceRequests() {
        final DescribeSpotInstanceRequestsRequest request = new DescribeSpotInstanceRequestsRequest().withFilters(id.asFilter());
        final DescribeSpotInstanceRequestsResult result = ec2.describeSpotInstanceRequests(request);
        return result.getSpotInstanceRequests();
    }

    public List<String> getSpotInstanceRequestIds() {
        return Aws.getSpotInstanceRequestIds(getSpotInstanceRequests());
    }

    public List<String> getPublicDnsNames() {
        return getInstances().stream().map(Instance::getPublicDnsName).collect(Collectors.toList());
    }

    public List<String> getPrivateDnsNames() {
        return getInstances().stream().map(Instance::getPrivateDnsName).collect(Collectors.toList());
    }

    public List<Tag> getTags() {
        return getInstances().stream()
                .flatMap(instance -> instance.getTags().stream())
                .distinct()
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .collect(Collectors.toList());
    }

}
