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

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.RequestSpotInstancesRequest;
import com.amazonaws.services.ec2.model.RequestSpotInstancesResult;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;

public class JsonLoggerTest extends Assert {

    @Test
    public void test() throws Exception {

        final AmazonEC2Adapter client = new AmazonEC2Adapter() {
            @Override
            public RequestSpotInstancesResult requestSpotInstances(RequestSpotInstancesRequest requestSpotInstancesRequest) throws AmazonClientException {
                return new RequestSpotInstancesResult().withSpotInstanceRequests(
                    new SpotInstanceRequest()
                        .withInstanceId("asdf12345")
                        .withSpotPrice("0.0008")
                        .withCreateTime(new Date(1437861848)),
                    new SpotInstanceRequest()
                        .withInstanceId("qwert34576")
                        .withSpotPrice("0.0109")
                        .withCreateTime(new Date(1434861848))
                );
            }
        };
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        final AmazonEC2 amazonEC2 = (AmazonEC2) Proxy.newProxyInstance(contextClassLoader, new Class[]{AmazonEC2.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(client, args);
            }
        });

        final RequestSpotInstancesResult requestSpotInstancesResult = amazonEC2.requestSpotInstances(new RequestSpotInstancesRequest());

        assertEquals(2, requestSpotInstancesResult.getSpotInstanceRequests().size());
    }

}