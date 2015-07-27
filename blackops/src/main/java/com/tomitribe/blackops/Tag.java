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

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getKey() == null) ? 0 : getKey().hashCode());
        hashCode = prime * hashCode + ((getValue() == null) ? 0 : getValue().hashCode());
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (!(obj instanceof Tag)) return false;
        Tag other = (Tag) obj;

        if (other.getKey() == null ^ this.getKey() == null) return false;
        if (other.getKey() != null && !other.getKey().equals(this.getKey())) return false;
        if (other.getValue() == null ^ this.getValue() == null) return false;
        if (other.getValue() != null && !other.getValue().equals(this.getValue())) return false;
        return true;
    }

}
