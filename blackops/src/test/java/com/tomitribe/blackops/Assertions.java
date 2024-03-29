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

import com.amazonaws.services.ec2.model.*;
import org.junit.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Utility methods to "Rubber stamp" test results
 *
 * When code is stable and the expected outcome can be verified (usually manually),
 * generating asserts that would take a very long time to type is a great way
 * to test the full expected output and cement the expected result.
 *
 * This code is usually once and deleted, however change does occur so keeping
 * the generation code is a very good practice.
 *
 * This also helps others on the team see and be encouraged to follow the same
 * practice of simple code generation to improve test coverage.
 */
public class Assertions {


    public static void generateInstanceAssert(final List<Instance> instances) {
        instances.forEach(next -> System.out.printf("assertInstance(\"%s\", \"%s\", \"%s\", \"%s\", iterator.next());%n",
                next.getInstanceId(), next.getSpotInstanceRequestId(), next.getState().getName(), next.getPublicDnsName()));
        System.out.println("assertFalse(iterator.hasNext());");
    }

    public static void assertInstance(final String instanceId, final String spotRequestId, final String state, final String publicDns, final Instance instance) {
        Assert.assertEquals(instanceId, instance.getInstanceId());
        Assert.assertEquals(spotRequestId, instance.getSpotInstanceRequestId());
        Assert.assertEquals(state, instance.getState().getName());
        Assert.assertEquals(publicDns, instance.getPublicDnsName());
    }

    public static void assertTag(final String key, final String value, final com.amazonaws.services.ec2.model.Tag instance) {
        Assert.assertEquals(key, instance.getKey());
        Assert.assertEquals(value, instance.getValue());
    }

    public static void assertSpotInstanceRequest(String spotRequestInstanceId, String state, String instanceId, SpotInstanceRequest request) {
        Assert.assertEquals(spotRequestInstanceId, request.getSpotInstanceRequestId());
        Assert.assertEquals(state, request.getState());
        Assert.assertEquals(instanceId, request.getInstanceId());
    }

    public static void generateSpotInstanceRequestAssertions(List<SpotInstanceRequest> requests) {
        requests.forEach(next -> System.out.printf("assertSpotInstanceRequest(\"%s\",\"%s\",\"%s\", iterator.next());%n", next.getSpotInstanceRequestId(), next.getState(), next.getInstanceId()));
        System.out.println("assertFalse(iterator.hasNext());");
    }

    public static void assertState(String name, int count, Map.Entry<String, State> entry) {
        Assert.assertEquals(name, entry.getKey());
        Assert.assertEquals(name, entry.getValue().getName());
        Assert.assertEquals(count, entry.getValue().getCount());
    }

    public static void assertFilter(Filter filter, final String expectedName, final String... expectedValues) {
        Assert.assertEquals(expectedName, filter.getName());

        final Iterator<String> values = filter.getValues().iterator();
        Stream.of(expectedValues).forEach(s -> Assert.assertEquals(s, values.next()));
        Assert.assertFalse(values.hasNext());
    }

    public static void generateStateAsserts(Map<String, State> states) {
        states.entrySet().forEach(entry -> System.out.printf("assertState(\"%s\", %s, iterator.next());%n", entry.getKey(), entry.getValue().getCount()));
        System.out.println("assertFalse(iterator.hasNext());");
    }

    public static void generateStringAssertions(List<String> ids) {
        ids.forEach(s -> System.out.printf("assertEquals(\"%s\", iterator.next());%n", s));
        System.out.println("assertFalse(iterator.hasNext());");
    }

    public static void generateEnumAssertions(List<? extends Enum> ids) {
        ids.forEach(s -> System.out.printf("assertEquals(%s.%s, iterator.next());%n", s.getDeclaringClass().getSimpleName(), s.name()));
        System.out.println("assertFalse(iterator.hasNext());");
    }

    public static void generateTagAssertions(List<com.amazonaws.services.ec2.model.Tag> tags) {
        tags.stream().forEach(tag -> System.out.printf("assertTag(\"%s\", \"%s\", iterator.next());%n", tag.getKey(), tag.getValue()));
        System.out.println("assertFalse(iterator.hasNext());");
    }
}
