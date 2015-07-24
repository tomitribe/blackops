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

import org.apache.johnzon.mapper.Mapper;
import org.apache.johnzon.mapper.MapperBuilder;
import org.tomitribe.util.PrintString;

import java.util.Comparator;

public class Json {

    public static String toString(final Object object) {
        final Mapper mapper = new MapperBuilder()
            .setPretty(true)
            .setAttributeOrder(Comparator.<String>naturalOrder())
            .build();

        final PrintString string = new PrintString();
        mapper.writeObject(object, string);
        return string.toString();
    }
}