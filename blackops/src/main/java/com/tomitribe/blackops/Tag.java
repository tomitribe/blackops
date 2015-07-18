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

public class Tag {
    private final String key;
    private final String value;

    public Tag(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static Tag parse(final String raw) {
        final String[] split = raw.split(" *= *");

        if (split.length == 1) {
            return new Tag(split[0], null);
        }

        if (split.length == 2) {
            return new Tag(split[0], split[1]);
        }

        throw new IllegalArgumentException("Invalid tag format: " + raw);
    }
}
