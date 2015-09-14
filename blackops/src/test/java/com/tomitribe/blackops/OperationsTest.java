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
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import com.amazonaws.services.ec2.model.SpotInstanceState;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static com.amazonaws.services.ec2.model.InstanceStateName.Pending;
import static com.amazonaws.services.ec2.model.InstanceStateName.Running;
import static org.junit.Assert.*;

public class OperationsTest extends Assert {

    /**
     * Entries should be sorted
     * When there are no values to format the text "none" should be used
     */
    @Test
    public void testFormatStatus() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final List<Instance> instances = operation.getInstances();
        final List<SpotInstanceRequest> spotInstanceRequests = operation.getSpotInstanceRequests();

        {
            final String status = Operations.formatStatus(instances, spotInstanceRequests);
            assertEquals("Instances: pending (3), running (4), shutting-down (2), stopped (1), stopping (2), terminated (4) - " +
                    "SpotRequests: active (7), closed (4), open (3)", status);
        }

        instances.clear();

        {
            final String status = Operations.formatStatus(instances, spotInstanceRequests);
            assertEquals("Instances: none - SpotRequests: active (7), closed (4), open (3)", status);
        }

        spotInstanceRequests.clear();

        {
            final String status = Operations.formatStatus(instances, spotInstanceRequests);
            assertEquals("Instances: none - SpotRequests: none", status);
        }
    }
}