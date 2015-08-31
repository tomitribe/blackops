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
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.tomitribe.blackops.Assertions.assertSpotInstanceRequest;

public class OperationBuilderTest extends Assert {
    public static void main(String[] args) {
        final DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
        describeInstancesRequest.getFilters().add(new Filter("image-id").withValues("ami-0116c16a"));
        final DescribeInstancesResult describeInstancesResult = Aws.client().describeInstances(describeInstancesRequest);
        final List<Instance> instances = Aws.getInstances(describeInstancesResult);

        final List<Instance> filtered = instances.stream()
                .filter(instance -> instance.getTags().size() > 0)
                .collect(Collectors.toList());

        System.out.println(instances.size());
        System.out.println(filtered.size());
        final List<String> instanceIds = Aws.getInstanceIds(filtered);

//        final TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest(instanceIds);
//        Aws.client().terminateInstances(terminateInstancesRequest);
    }
    @Test
    public void testBuild() throws Exception {
        final AmazonEC2 client = AmazonEC2Builder.fromCurrentTestMethod()
                .withFilter(MockData::maskUserData)
                .withFilter(MockData::maskOperationId)
                .build();

        final String accessKey = Aws.credentials.getAWSAccessKeyId();
        final String secretKey = Aws.credentials.getAWSSecretKey();
        final OperationBuilder builder = new OperationBuilder("OperationBuilderTest", accessKey, secretKey, client);

        builder.operation().script("date > /tmp/foo.txt");
        builder.specification().withInstanceType(InstanceType.M3Medium);
        builder.request().withInstanceCount(4);

        final Operation operation = builder.build();

        final List<SpotInstanceRequest> list = operation.getSpotInstanceRequests();

        final Iterator<SpotInstanceRequest> iterator = list.iterator();
        assertSpotInstanceRequest("sir-024z243a", "open", null, iterator.next());
        assertSpotInstanceRequest("sir-0251ct2e", "open", null, iterator.next());
        assertSpotInstanceRequest("sir-0253df5n", "open", null, iterator.next());
        assertSpotInstanceRequest("sir-0254b6va", "open", null, iterator.next());

    }
}