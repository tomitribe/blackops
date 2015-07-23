/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested
 * of its trade secrets, irrespective of what has been deposited with the
 * U.S. Copyright Office.
 *
 */
package com.tomitribe.blackops;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StatesTest extends Assert {

    private List<String> strings;

    @Before
    public void setUp() throws Exception {
        strings = Arrays.asList(
                "green",
                "green",
                "red",
                "red",
                "red",
                "red",
                "blue",
                "green",
                "red"
        );
    }

    @Test
    public void testCount() throws Exception {

        final Map<String, State> count = States.count(strings, Object::toString);

        assertEquals("red", count.get("red").getName());
        assertEquals(5, count.get("red").getCount());

        assertEquals("green", count.get("green").getName());
        assertEquals(3, count.get("green").getCount());

        assertEquals("blue", count.get("blue").getName());
        assertEquals(1, count.get("blue").getCount());
    }

    @Test
    public void printStates() throws Exception {
        final Map<String, State> count = States.count(strings, Object::toString);

        assertEquals("blue (1), green (3), red (5)", States.printStates(count));
    }
}