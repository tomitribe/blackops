package com.tomitribe.blackops;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest extends Assert {

    @Test
    public void testParse() throws Exception {
        final Tag parse = Tag.parse("color = orange");

        assertEquals("color", parse.getKey());
        assertEquals("orange", parse.getValue());
    }
}