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
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class OperationBuilderTest extends Assert {

    @Test
    @Ignore
    public void testBuild() throws Exception {
        final String accessKey = "AKIAJZ4VDNQFF7757XMQ";
        final String secretKey = "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";

        final AmazonEC2 client = Aws.client();
        final OperationBuilder builder = new OperationBuilder("OperationBuilderTest", accessKey, secretKey, client);

        builder.operation().script("date > /tmp/foo.txt");
        builder.specification().withInstanceType(InstanceType.M3Medium);
        builder.request().withInstanceCount(4);

        final Operation operation = builder.build();

        final List<SpotInstanceRequest> list = operation.getSpotInstanceRequests();

    }
}