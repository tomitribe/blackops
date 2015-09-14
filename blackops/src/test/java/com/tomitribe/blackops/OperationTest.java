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
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import com.amazonaws.services.ec2.model.SpotInstanceState;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.tomitribe.util.PrintString;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.amazonaws.services.ec2.model.InstanceStateName.Running;
import static com.amazonaws.services.ec2.model.InstanceStateName.Terminated;
import static com.tomitribe.blackops.Assertions.assertInstance;
import static com.tomitribe.blackops.Assertions.assertSpotInstanceRequest;
import static com.tomitribe.blackops.Assertions.assertState;
import static com.tomitribe.blackops.Assertions.assertTag;

public class OperationTest extends Assert {

    @Test
    public void testGetInstances() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final List<Instance> instances = operation.getInstances();
        final Iterator<Instance> iterator = instances.iterator();
        assertInstance("i-90b43b79", "sir-02ehdb1r", "terminated", "", iterator.next());
        assertInstance("i-3bb03fd2", "sir-02enh0sr", "terminated", "", iterator.next());
        assertInstance("i-91b43b78", "sir-02ehmelq", "terminated", "", iterator.next());
        assertInstance("i-1eb33cf7", "sir-02elghja", "terminated", "", iterator.next());
        assertInstance("i-eccc4d05", "sir-02eh6zfl", "running", "ec2-54-237-110-193.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-efcc4d06", "sir-02egthbr", "running", "ec2-54-90-58-137.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-42cc4dab", "sir-02ep34v9", "running", "ec2-54-237-26-16.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-32ce4fdb", "sir-02emmkv4", "running", "ec2-54-237-8-49.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-bbce4f52", "sir-02ep7rs7", "running", "ec2-54-237-69-184.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-83cf4e6a", "sir-02eldnx3", "running", "ec2-54-157-138-7.compute-1.amazonaws.com", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSpotInstanceRequests() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final List<SpotInstanceRequest> spotInstanceRequests = operation.getSpotInstanceRequests();
        final Iterator<SpotInstanceRequest> iterator = spotInstanceRequests.iterator();
        assertSpotInstanceRequest("sir-02efzkef", "active", "i-7d0c83d5", iterator.next());
        assertSpotInstanceRequest("sir-02ep34v9", "active", "i-42cc4dab", iterator.next());
        assertSpotInstanceRequest("sir-02ep7rs7", "active", "i-bbce4f52", iterator.next());
        assertSpotInstanceRequest("sir-02egthbr", "active", "i-efcc4d06", iterator.next());
        assertSpotInstanceRequest("sir-02eh6zfl", "active", "i-eccc4d05", iterator.next());
        assertSpotInstanceRequest("sir-02eldnx3", "active", "i-83cf4e6a", iterator.next());
        assertSpotInstanceRequest("sir-02emmkv4", "active", "i-32ce4fdb", iterator.next());
        assertSpotInstanceRequest("sir-02ehdb1r", "closed", "i-90b43b79", iterator.next());
        assertSpotInstanceRequest("sir-02ehmelq", "closed", "i-91b43b78", iterator.next());
        assertSpotInstanceRequest("sir-02elghja", "closed", "i-1eb33cf7", iterator.next());
        assertSpotInstanceRequest("sir-02enh0sr", "closed", "i-3bb03fd2", iterator.next());
        assertSpotInstanceRequest("sir-02ejzb35", "open", null, iterator.next());
        assertSpotInstanceRequest("sir-02ekm3qz", "open", null, iterator.next());
        assertSpotInstanceRequest("sir-02enlfak", "open", null, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetPublicDnsNames() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final List<String> publicDnsNames = operation.getPublicDnsNames();

        final Iterator<String> iterator = publicDnsNames.iterator();
        assertEquals("", iterator.next());
        assertEquals("", iterator.next());
        assertEquals("", iterator.next());
        assertEquals("", iterator.next());
        assertEquals("ec2-54-237-110-193.compute-1.amazonaws.com", iterator.next());
        assertEquals("ec2-54-90-58-137.compute-1.amazonaws.com", iterator.next());
        assertEquals("ec2-54-237-26-16.compute-1.amazonaws.com", iterator.next());
        assertEquals("ec2-54-237-8-49.compute-1.amazonaws.com", iterator.next());
        assertEquals("ec2-54-237-69-184.compute-1.amazonaws.com", iterator.next());
        assertEquals("ec2-54-157-138-7.compute-1.amazonaws.com", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetPrivateDnsNames() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final List<String> privateDnsNames = operation.getPrivateDnsNames();

        final Iterator<String> iterator = privateDnsNames.iterator();
        assertEquals("", iterator.next());
        assertEquals("", iterator.next());
        assertEquals("", iterator.next());
        assertEquals("", iterator.next());
        assertEquals("ip-10-232-138-224.ec2.internal", iterator.next());
        assertEquals("ip-10-180-36-70.ec2.internal", iterator.next());
        assertEquals("ip-10-233-127-108.ec2.internal", iterator.next());
        assertEquals("ip-10-7-181-228.ec2.internal", iterator.next());
        assertEquals("ip-10-203-203-46.ec2.internal", iterator.next());
        assertEquals("ip-10-109-180-253.ec2.internal", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetTags() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final List<com.amazonaws.services.ec2.model.Tag> tags = operation.getTags();
        final Iterator<com.amazonaws.services.ec2.model.Tag> iterator = tags.iterator();
        assertTag("Name", "20c401df6e75209b2bcbf6a1ec6949bd172f53d6", iterator.next());
        assertTag("Name", "null", iterator.next());
        assertTag("operation-id", "20c401df6e75209b2bcbf6a1ec6949bd172f53d6", iterator.next());
        assertTag("operation-id", "834a27d7cb74cc5e99068bd395bbe9d45a42e871", iterator.next());
        assertTag("operation-id", "816058206985f37d95f8b9f091dd35d78f048db5", iterator.next());
        assertTag("shutdown", "false", iterator.next());
        assertTag("user.name", "dblevins", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetInstanceIds() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final List<String> instanceIds = operation.getInstanceIds();
        final Iterator<String> iterator = instanceIds.iterator();
        assertEquals("i-90b43b79", iterator.next());
        assertEquals("i-3bb03fd2", iterator.next());
        assertEquals("i-91b43b78", iterator.next());
        assertEquals("i-1eb33cf7", iterator.next());
        assertEquals("i-eccc4d05", iterator.next());
        assertEquals("i-efcc4d06", iterator.next());
        assertEquals("i-42cc4dab", iterator.next());
        assertEquals("i-32ce4fdb", iterator.next());
        assertEquals("i-bbce4f52", iterator.next());
        assertEquals("i-83cf4e6a", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSpotInstanceRequestIds() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final List<String> spotInstanceRequestIds = operation.getSpotInstanceRequestIds();
        final Iterator<String> iterator = spotInstanceRequestIds.iterator();
        assertEquals("sir-02efzkef", iterator.next());
        assertEquals("sir-02ep34v9", iterator.next());
        assertEquals("sir-02ep7rs7", iterator.next());
        assertEquals("sir-02egthbr", iterator.next());
        assertEquals("sir-02eh6zfl", iterator.next());
        assertEquals("sir-02eldnx3", iterator.next());
        assertEquals("sir-02emmkv4", iterator.next());
        assertEquals("sir-02ehdb1r", iterator.next());
        assertEquals("sir-02ehmelq", iterator.next());
        assertEquals("sir-02elghja", iterator.next());
        assertEquals("sir-02enh0sr", iterator.next());
        assertEquals("sir-02ejzb35", iterator.next());
        assertEquals("sir-02ekm3qz", iterator.next());
        assertEquals("sir-02enlfak", iterator.next());
        assertFalse(iterator.hasNext());
    }


    @Test
    public void testCountInstanceStates() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final Map<String, State> states = operation.countInstanceStates();

        final Iterator<Map.Entry<String, State>> iterator = states.entrySet().iterator();
        assertState("running", 6, iterator.next());
        assertState("terminated", 4, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCountSpotInstanceRequestStates() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final Map<String, State> states = operation.countSpotInstanceRequestStates();

        final Iterator<Map.Entry<String, State>> iterator = states.entrySet().iterator();
        assertState("active", 7, iterator.next());
        assertState("closed", 4, iterator.next());
        assertState("open", 3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testTerminateInstances() throws Exception {

    }

    @Test
    public void testCancelSpotInstanceRequests() throws Exception {

    }

    @Test
    @Ignore
    public void testExpandCapacityTo() throws Exception {
        final Operation operation = new Operation(OperationId.generate(), Aws.client());

        operation.expandCapacityTo(6);
        while (operation.getInstances(Running).size() != 6) {
            Thread.sleep(2000);
            operation.getAnticipatedCapacity();
        }
    }

    @Test
    public void testGetSpotRequestsWithState() throws IOException, ClassNotFoundException {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final List<SpotInstanceRequest> requests = operation.getSpotInstanceRequests(SpotInstanceState.Cancelled);

        final Iterator<SpotInstanceRequest> iterator = requests.iterator();
        assertSpotInstanceRequest("sir-02enlfak", "cancelled", "i-d8159a31", iterator.next());
    }

    @Test
    public void testGetInstancesRunning() throws IOException, ClassNotFoundException {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final List<Instance> instances = operation.getInstances(Running);
        final Iterator<Instance> iterator = instances.iterator();
        assertInstance("i-63159a8a", "sir-02ekm3qz", "running", "ec2-54-237-127-17.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-22159acb", "sir-02ejzb35", "running", "ec2-54-237-122-168.compute-1.amazonaws.com", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetInstancesRunningAndTerminated() throws IOException, ClassNotFoundException {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final List<Instance> instances = operation.getInstances(Running, Terminated);

        final Iterator<Instance> iterator = instances.iterator();
        assertInstance("i-63159a8a", "sir-02ekm3qz", "running", "ec2-54-237-127-17.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-22159acb", "sir-02ejzb35", "running", "ec2-54-237-122-168.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-d8159a31", "sir-02enlfak", "terminated", "", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCapacity() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-xn8w64hsg3dmb"), amazonEC2);

        final int capacity = operation.getAnticipatedCapacity();
        assertEquals(3, capacity);
    }

    @Test
    public void testAwaitCapacity() throws Exception {
        final AmazonEC2 amazonEC2 = AmazonEC2Builder.fromCurrentTestMethod().build();

        final Operation operation = new Operation(OperationId.parse("op-gere5lbkoguy6"), amazonEC2);

        final PrintString out = new PrintString();
        final List<Instance> instances = operation.awaitCapacity(3, 1, TimeUnit.MILLISECONDS, 10, TimeUnit.MINUTES, out::println);

        assertEquals("Instances: none - SpotRequests: open (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: open (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: open (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: open (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: open (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: open (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: active (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: active (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: active (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: active (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: active (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: active (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: active (3) - 0 seconds\n" +
                "Instances: none - SpotRequests: active (3) - 0 seconds\n" +
                "Instances: running (3) - SpotRequests: active (3) - 0 seconds\n", out.toString());

        final Iterator<Instance> iterator = instances.iterator();
        assertInstance("i-29463cc0", "sir-02ed0mef", "running", "ec2-54-144-136-64.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-1c463cf5", "sir-02e7ljm2", "running", "ec2-54-90-15-210.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-82463c6b", "sir-02eagg0n", "running", "ec2-54-146-77-143.compute-1.amazonaws.com", iterator.next());
        assertFalse(iterator.hasNext());

    }

}