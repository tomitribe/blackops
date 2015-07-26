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
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.InstanceStateName;

public class DescribeInstances {

    private final DescribeInstancesRequest request = new DescribeInstancesRequest();

    public DescribeInstances withTag(final String key, final String value) {
        return withTag(new Tag(key, value));
    }

    public DescribeInstances withState(final InstanceStateName state) {
        request.withFilters(new Filter("instance-state-name").withValues(state.toString()));
        return this;
    }

    public DescribeInstances withTags(final Iterable<Tag> tag) {
        for (final Tag t : tag) {
            withTag(t);
        }
        return this;
    }

    public DescribeInstances withTag(final Tag tag) {

        if (tag.getValue() == null) {

            request.withFilters(new Filter("tag-key").withValues(tag.getKey()));

        } else {

            request.withFilters(new Filter("tag:" + tag.getKey()).withValues(tag.getValue()));

        }
        return this;
    }

    public DescribeInstances withOperationId(final String id) {
        request.withFilters(new OperationId(id).asFilter());
        return this;
    }

    public DescribeInstancesRequest getRequest() {
        return request;
    }
}
