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

import com.amazonaws.services.ec2.AmazonEC2;
import org.tomitribe.util.IO;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

public class MockEC2Client {

    public static AmazonEC2 create(InvocationHandler h) {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final Class[] interfaces = {AmazonEC2.class};

        return (AmazonEC2) Proxy.newProxyInstance(loader, interfaces, h);
    }

    public static AmazonEC2 create(final Object... responses) {
        final Iterator<Object> iterator = Arrays.asList(responses).iterator();
        return create((proxy, method, args) -> iterator.next());
    }

    public static AmazonEC2 fromJson(String... resources) throws ClassNotFoundException, IOException {
        return create(Stream.of(resources).map(MockEC2Client::loadResponse).toArray());
    }

    public static Object loadResponse(String name) {
        final ClassLoader classLoader = MockEC2Client.class.getClassLoader();
        try {
            final String type = name.split("-")[1];
            final URL resource = classLoader.getResource(name);
            final Class clazz = classLoader.loadClass("com.amazonaws.services.ec2.model." + type);
            return Json.fromString(IO.slurp(resource), clazz);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
