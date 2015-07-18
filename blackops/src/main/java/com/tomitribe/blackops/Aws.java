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

import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateName;

import java.util.List;
import java.util.stream.Collectors;

public enum Aws {
    ;

    public static List<Instance> getInstances(final DescribeInstancesResult result) {
        return (List<Instance>) result.getReservations().stream()
                .flatMap(reservation -> reservation.getInstances().stream())
                .sorted((o1, o2) -> o2.getLaunchTime().compareTo(o1.getLaunchTime()))
                .collect(Collectors.toList());
    }

    public static DescribeInstancesRequest describeInstancesRequest(final List<Tag> tag) {
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
}
