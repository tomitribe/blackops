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

import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.util.Base32;
import org.tomitribe.util.Longs;
import org.tomitribe.util.hash.Slice;
import org.tomitribe.util.hash.Slices;
import org.tomitribe.util.hash.XxHash64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;

public class OperationId {

    public static final String ID = "operation-id";
    private static final Pattern format = Pattern.compile("op-[a-z0-9]{13}");
    private final String id;

    @Deprecated
    public OperationId(String id) {
        this.id = id;
    }

    public static OperationId generate() {

        final byte[] bytes;

        try {
            // Generate some Random Data
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.getProperties().store(out, "" + System.currentTimeMillis() + new Random().nextDouble());
            out.flush();

            // XxHash64 hash it
            final byte[] array = out.toByteArray();
            final Slice data = Slices.wrappedBuffer(array);
            final long hash = XxHash64.hash(data);
            bytes = Longs.toBytes(hash);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        // Base32 encode it
        return new OperationId("op-" + Base32.encodeAsString(bytes).replaceAll("=", "").toLowerCase());
    }

    public static OperationId parse(final String id) {
        if (id == null) throw new IllegalArgumentException("id cannot be null");
        if (!format.matcher(id).matches()) throw new IllegalArgumentException("Invalid id format. Expecting " + format.pattern());
        return new OperationId(id);
    }

    public String get() {
        return id;
    }

    public Filter asFilter() {
        return new Filter("tag:" + ID).withValues(id);
    }

    public com.amazonaws.services.ec2.model.Tag asTag() {
        return new com.amazonaws.services.ec2.model.Tag(ID, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperationId that = (OperationId) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
