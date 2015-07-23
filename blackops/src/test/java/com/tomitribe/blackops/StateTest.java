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
import org.junit.Test;

public class StateTest extends Assert {

    @Test
    public void testToString() throws Exception {
        final State red = new State("red");
        red.incrementAndGet();
        red.incrementAndGet();
        red.incrementAndGet();
        red.incrementAndGet();

        assertEquals("red (4)", red.toString());
    }

}