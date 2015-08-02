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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;

public class EC2ResponseLogger {

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
        return object;
    }
}
