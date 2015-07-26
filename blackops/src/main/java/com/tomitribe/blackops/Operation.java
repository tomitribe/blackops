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
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsRequest;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;

import java.util.List;
import java.util.stream.Collectors;

public class Operation {

    private final OperationId id;

    private final AmazonEC2 ec2;

    public Operation(final OperationId id, final AmazonEC2 ec2) {
        this.id = id;
        this.ec2 = ec2;
    }

    public List<Instance> getInstances() {
        final DescribeInstancesResult result = ec2.describeInstances(new DescribeInstances().withOperationId(id.get()).getRequest());
        return Aws.getInstances(result);
    }

    public List<SpotInstanceRequest> getSpotInstanceRequets() {
        final DescribeSpotInstanceRequestsRequest request = new DescribeSpotInstanceRequestsRequest().withFilters(id.asFilter());
        final DescribeSpotInstanceRequestsResult result = ec2.describeSpotInstanceRequests(request);
        return result.getSpotInstanceRequests();
    }

    public List<String> getPublicDnsNames() {
        return getInstances().stream().map(Instance::getPublicDnsName).collect(Collectors.toList());
    }

    public List<String> getPrivateDnsNames() {
        return getInstances().stream().map(Instance::getPrivateDnsName).collect(Collectors.toList());
    }

}
