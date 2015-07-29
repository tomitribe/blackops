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
import com.fasterxml.jackson.databind.SerializationFeature;
import org.tomitribe.util.PrintString;

import java.io.IOException;

public class Json {

    private Json() {
    }

    public static String toString(final Object object) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

        try {
            final PrintString out = new PrintString();
            mapper.writerWithDefaultPrettyPrinter().writeValue(out, object);
            return scrub(out.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Not sure of another way to prevent Jackson from writing this element
     *
     * Neither WRITE_NULL_MAP_VALUES nor WRITE_EMPTY_JSON_ARRAYS seem to work
     */
    private static String scrub(final String string) {
        return string.replace("  \"progressListener\" : { },", "");
    }

    public static <T> T fromString(final String json, final Class<T> clazz) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        try {
            return mapper.reader().forType(clazz).readValue(json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}