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
import org.junit.Assert;
import org.tomitribe.util.IO;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MockEC2Client {

    private static final Pattern userDataMask = Pattern.compile("\"userData\" : \"([^\"]+)\"");
    private static final Pattern operationIdMask = Pattern.compile("\"op-([^\"]+)\"");

    public static AmazonEC2 create(final InvocationHandler h) {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final Class[] interfaces = {AmazonEC2.class};

        return (AmazonEC2) Proxy.newProxyInstance(loader, interfaces, h);
    }

    public static AmazonEC2 create(final Object... objects) {
        final Iterator<Object> iterator = Arrays.asList(objects).iterator();
        return create(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (args != null && args.length == 1) {
                    compare(iterator.next(), args[0]);
                }
                if (Void.TYPE.equals(method.getReturnType())) {
                    return null;
                }
                return iterator.next();
            }
        });
    }

    private static void compare(final Object expected, final Object actual) {
        final String jsonA = normalize(Json.toString(expected));
        final String jsonB = normalize(Json.toString(actual));
        Assert.assertEquals(jsonA, jsonB);
    }


    private static String normalize(String json) {
        json = mask(json, operationIdMask);
        json = mask(json, userDataMask);
        return json;
    }

    private static String mask(String json, Pattern compile) {
        final Matcher m = compile.matcher(json);
        if (m.find()) {
            final String data = m.group(1);
            final String mask = data.replaceAll(".", "x");
            json = json.replace(data, mask);
        }

        return json;
    }

    public static AmazonEC2 fromJson(String... resources) throws ClassNotFoundException, IOException {
        return create(Stream.of(resources).map(MockEC2Client::load).toArray());
    }

    public static Object load(String name) {
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
