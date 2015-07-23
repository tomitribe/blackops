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

public class TagTest extends Assert {

    @Test
    public void testParse() throws Exception {
        final Tag parse = Tag.parse("color = orange");

        assertEquals("color", parse.getKey());
        assertEquals("orange", parse.getValue());
    }
}