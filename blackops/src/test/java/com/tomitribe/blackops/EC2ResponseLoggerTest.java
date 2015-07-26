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

import java.util.Date;

public class EC2ResponseLoggerTest extends Assert {

    @Test
    public void test() throws Exception {

        final AmazonEC2 amazonEC2 = EC2ResponseLogger.wrap(new AmazonEC2Adapter() {
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
        });

        final RequestSpotInstancesResult requestSpotInstancesResult = amazonEC2.requestSpotInstances(new RequestSpotInstancesRequest());

        assertEquals(2, requestSpotInstancesResult.getSpotInstanceRequests().size());
    }

}