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

import com.amazonaws.services.ec2.model.Instance;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

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
public class AssertGeneration {

    public static void generateStateAsserts(Map<String, State> states) {
        states.entrySet().forEach(next -> {
                    System.out.println("{");
                    System.out.printf("    final Map.Entry<String, State> entry = iterator.next();%n");
                    System.out.printf("    assertEquals(\"%s\", entry.getKey());%n", next.getKey());
                    System.out.printf("    assertEquals(\"%s\", entry.getValue().getName());%n", next.getValue().getName());
                    System.out.printf("    assertEquals(%s, entry.getValue().getCount());%n", next.getValue().getCount());
                    System.out.printf("    assertEquals(\"%s\", entry.getValue().toString());%n", next.getValue().toString());
                    System.out.println("}");
                }
        );
    }

    public static void generateInstanceAssert(List<Instance> instances) {
        instances.forEach(next -> System.out.printf("assertInstance(\"%s\", \"%s\", \"%s\", \"%s\", iterator.next());%n", next.getInstanceId(), next.getSpotInstanceRequestId(), next.getState().getName(), next.getPublicDnsName()));
    }

    public static void assertInstance(String instanceId, String spotRequestId, String state, String publicDns, Instance instance) {
        Assert.assertEquals(instanceId, instance.getInstanceId());
        Assert.assertEquals(spotRequestId, instance.getSpotInstanceRequestId());
        Assert.assertEquals(state, instance.getState().getName());
        Assert.assertEquals(publicDns, instance.getPublicDnsName());
    }
}
