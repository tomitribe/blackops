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
import org.tomitribe.util.Files;
import org.tomitribe.util.IO;
import org.tomitribe.util.JarLocation;
import org.tomitribe.util.reflect.StackTraceElements;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.stream.Stream;

public class EC2ResponseLogger {

    private static ThreadLocal<File> dir = new ThreadLocal<>();

    private EC2ResponseLogger() {
    }

    public static AmazonEC2 wrap(final AmazonEC2 client) {

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final Class[] interfaces = {AmazonEC2.class};

        return (AmazonEC2) Proxy.newProxyInstance(loader, interfaces, (proxy, method, args) -> {
            if (args != null && args.length == 1) {
                record(args[0]);
            }
            return record(method.invoke(client, args));
        });
    }

    public static Object record(final Object object) throws IOException {
        if (object == null) return null;

        final File file = File.createTempFile(System.currentTimeMillis() + "-" + object.getClass().getSimpleName() + "-", ".json");
        System.out.println(file.getAbsolutePath());
        IO.copy(IO.read(Json.toString(object)), file);

        // If test logging enabled, keep a copy
        if (dir.get() != null) {
            final File save = new File(dir.get(), file.getName());
            IO.copy(file, save);
        }

        return object;
    }

    public static File getBase(final Class clazz, final String method) {
        final File testClasses = JarLocation.jarLocation(clazz);
        final File module = testClasses.getParentFile().getParentFile();
        return Files.file(module, "src", "test", "resources", clazz.getSimpleName(), method);
    }

    public static void recordCurrentTestMethod() {
        try {
            final StackTraceElement caller = StackTraceElements.getCallingMethod();
            final Class<?> clazz = StackTraceElements.asClass(caller);

            final File testDir = Files.mkdirs(getBase(clazz, caller.getMethodName()));

            // Clean out old results
            Stream.of(testDir.listFiles()).forEach(File::delete);

            dir.set(testDir);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
