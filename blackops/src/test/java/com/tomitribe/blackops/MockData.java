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

import org.tomitribe.util.Files;
import org.tomitribe.util.IO;

import java.io.File;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MockData {

    private static final Pattern userDataMask = Pattern.compile("\"userData\" : \"([^\"]+)\"");
    private static final Pattern operationIdMask = Pattern.compile("\"op-([^\"]+)\"");

    public static Object[] objectsFromJson(String[] resources) {
        return Stream.of(resources).map(MockData::load).toArray();
    }

    public static Object[] objectsFromTestMethod(Class clazz, String method) {
        final File base = EC2ResponseLogger.getBase(clazz, method);
        Files.dir(base);
        return objectsFromDirectory(base);
    }

    public static Object[] objectsFromDirectory(final File base) {
        return Stream.of(base.listFiles())
                .filter(File::isFile)
                .filter(file -> file.getName().endsWith(".json"))
                .sorted(File::compareTo)
                .map(MockData::load)
                .toArray();
    }

    public static Object load(String name) {
        final ClassLoader classLoader = MockData.class.getClassLoader();
        try {
            final String type = name.split("-")[1];
            final URL resource = classLoader.getResource(name);
            final Class clazz = classLoader.loadClass("com.amazonaws.services.ec2.model." + type);
            return Json.fromString(IO.slurp(resource), clazz);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static Object load(File file) {
        final ClassLoader classLoader = MockData.class.getClassLoader();
        try {
            final String type = file.getName().split("-")[1];
            final Class clazz = classLoader.loadClass("com.amazonaws.services.ec2.model." + type);
            return Json.fromString(IO.slurp(file), clazz);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String maskUserData(final String string) {
        return mask(string, userDataMask);
    }

    public static String maskOperationId(final String string) {
        return mask(string, operationIdMask);
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
}
