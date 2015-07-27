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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tomitribe.util.PrintString;

import java.io.IOException;

public class Json {

    public static String toString(final Object object) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        try {
            final PrintString out = new PrintString();
            mapper.writeValue(out, object);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromString(final String json, final Class<T> clazz) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        try {
            return mapper.reader().forType(clazz).readValue(json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}