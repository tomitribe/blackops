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
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Reservation;
import org.junit.Assert;
import org.junit.Test;
import org.tomitribe.util.IO;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class OperationTest extends Assert {

    public static class Foo {
        private Date date;

        public Foo() {
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
    @Test
    public void testGetInstances() throws Exception {
        final Foo foo = new Foo();
        foo.setDate(new Date());

        System.out.println(Json.toString(foo));

        final URL resource = OperationTest.class.getClassLoader().getResource("1437882646414-DescribeInstancesResult-264183782752925532.json");
        final DescribeInstancesResult describeInstancesResult = Json.fromString(IO.slurp(resource), DescribeInstancesResult.class);
        final StaticResponseHandler h = new StaticResponseHandler(describeInstancesResult);


        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final Class[] interfaces = {AmazonEC2.class};

        final AmazonEC2 amazonEC2 = (AmazonEC2) Proxy.newProxyInstance(loader, interfaces, h);

        final DescribeInstancesResult result = amazonEC2.describeInstances(new DescribeInstancesRequest());

        for (final Reservation reservation : result.getReservations()) {
            System.out.println(reservation);
        }
    }

    @Test
    public void testGetSpotInstanceRequets() throws Exception {

    }

    @Test
    public void testGetPublicDnsNames() throws Exception {

    }

    @Test
    public void testGetPrivateDnsNames() throws Exception {

    }


    public static class StaticResponseHandler implements InvocationHandler {

        private final Iterator<?> iterator;

        public StaticResponseHandler(Object... responses) {
            this(Arrays.asList(responses).iterator());
        }

        public StaticResponseHandler(Iterator<?> iterator) {
            this.iterator = iterator;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return iterator.next();
        }
    }
}