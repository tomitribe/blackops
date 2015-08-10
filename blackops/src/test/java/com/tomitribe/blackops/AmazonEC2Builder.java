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
import org.tomitribe.util.reflect.StackTraceElements;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class AmazonEC2Builder {

    private final List<Object> mockdata = new ArrayList<>();
    private Function<String, String> filter = Function.identity();

    public static AmazonEC2Builder fromCurrentTestMethod() {
        try {
            final StackTraceElement callingMethod = StackTraceElements.getCallingMethod();
            final Class<?> clazz = StackTraceElements.asClass(callingMethod);

            final Object[] objects = MockData.objectsFromTestMethod(clazz, callingMethod.getMethodName());

            return new AmazonEC2Builder().withObjects(objects);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public AmazonEC2Builder withJson(String... json) {
        withObjects(MockData.objectsFromJson(json));
        return this;
    }

    public AmazonEC2Builder withObjects(Object... objects) {
        mockdata.addAll(Arrays.asList(objects));
        return this;
    }

    public AmazonEC2Builder withFilter(final Function<String, String> filter) {
        this.filter = this.filter.andThen(filter);
        return this;
    }

    public AmazonEC2 build() {
        return create(new AmazonEC2InvocationHandler(filter, mockdata));
    }

    public static AmazonEC2 create(final InvocationHandler h) {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final Class[] interfaces = {AmazonEC2.class};

        return (AmazonEC2) Proxy.newProxyInstance(loader, interfaces, h);
    }

    private static class AmazonEC2InvocationHandler implements InvocationHandler {
        private final Iterator<Object> iterator;
        private final Function<String, String> filter;

        private AmazonEC2InvocationHandler(final Function<String, String> filter, final Collection<Object> objects) {
            this.iterator = Collections.unmodifiableCollection(objects).iterator();
            this.filter = filter;
        }

        public void compare(final Object expected, final Object actual) {
            Assert.assertEquals(expected.getClass(), actual.getClass());

            final String jsonA = filter.apply(Json.toString(expected));
            final String jsonB = filter.apply(Json.toString(actual));

            Assert.assertEquals(jsonA, jsonB);
        }


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
    }
}
