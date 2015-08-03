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

import com.amazonaws.services.ec2.model.LaunchSpecification;
import com.amazonaws.services.ec2.model.RequestSpotInstancesResult;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import org.junit.Test;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

public class MarshallerTest {

    @Test
    public void test() throws Exception {
        final RequestSpotInstancesResult result = new RequestSpotInstancesResult();

        result.setSpotInstanceRequests(
                Arrays.asList(
                        new SpotInstanceRequest()
                                .withInstanceId("orange1234")
                                .withLaunchSpecification(
                                        new LaunchSpecification()
                                                .withUserData("13546-userdata-oiuy")
                                )
                        ,
                        new SpotInstanceRequest()
                                .withInstanceId("reduwu9")
                                .withLaunchSpecification(
                                        new LaunchSpecification()
                                                .withUserData("45qw5ut-userdata-uq4tjvnk")
                                )
                )
        );

        final String s = Json.toString(result);
        System.out.println(s);

    }
}
