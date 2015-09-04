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

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class AwaitTest extends Assert {

    @Test
    public void testCheck() throws Exception {
        final Iterator<String> iterator = Arrays.asList(null, null, null, null, "finished").iterator();
        final String check = Await.check(iterator::next, 0, 1, TimeUnit.MILLISECONDS);
        assertNotNull(check);
        assertEquals("finished", check);
    }

    @Test
    public void testCheckTimeout() throws Exception {
        final AtomicInteger count = new AtomicInteger();

        try {
            Await.check(() -> {
                count.incrementAndGet();
                return null;
            }, 0, 1, TimeUnit.MILLISECONDS, 10, TimeUnit.MILLISECONDS);
            fail("Expected TimeoutException");
        } catch (TimeoutException e) {
        }

        final int finalCount = count.get();
        assertTrue("" + finalCount, finalCount > 10 && finalCount < 20);
    }

    @Test
    public void testAwait() throws Exception {

    }
}