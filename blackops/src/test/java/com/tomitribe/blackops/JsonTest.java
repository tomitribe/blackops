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

import com.amazonaws.services.ec2.model.LaunchSpecification;
import com.amazonaws.services.ec2.model.RequestSpotInstancesRequest;
import org.junit.Assert;
import org.junit.Test;

public class JsonTest extends Assert {

    @Test
    public void test() throws Exception {

        final RequestSpotInstancesRequest request = new RequestSpotInstancesRequest()
                .withSpotPrice("0.02")
                .withInstanceCount(1)
                .withType("one-time")
                .withAvailabilityZoneGroup("us-east-1c")
                .withLaunchSpecification(new LaunchSpecification()
                        .withInstanceType("m3.medium")
                        .withImageId("ami-5bf23530") // Operative 1.0
                        .withMonitoringEnabled(false)
                        .withKeyName("tomitribe_dev")
                        .withSecurityGroups("Ports 60000+10"));

        final String json = Json.toString(request);
        System.out.println(json);

        assertContains(json, request.getSpotPrice());
        assertContains(json, request.getInstanceCount() + "");
        assertContains(json, request.getType());
        assertContains(json, request.getAvailabilityZoneGroup());
        assertContains(json, request.getLaunchSpecification().getInstanceType());
        assertContains(json, request.getLaunchSpecification().getImageId());
        assertContains(json, request.getLaunchSpecification().getKeyName());
        assertContains(json, request.getLaunchSpecification().getSecurityGroups().get(0));

        final RequestSpotInstancesRequest rehydrated = Json.fromString(json, RequestSpotInstancesRequest.class);
        final String rewritten = Json.toString(rehydrated);

        assertEquals(json, rewritten);
    }

    private void assertContains(String json, String value) {
        assertTrue(String.format("Does not contain '%s'", value), json.contains(value));
    }
}