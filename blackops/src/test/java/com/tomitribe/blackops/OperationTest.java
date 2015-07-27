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
import com.amazonaws.services.ec2.model.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OperationTest extends Assert {

    @Test
    public void testGetInstances() throws Exception {
        final AmazonEC2 amazonEC2 = MockEC2Client.fromJson("1437882646414-DescribeInstancesResult-264183782752925532.json");

        final Operation operation = new Operation(new OperationId("asdfghjk234567"), amazonEC2);

        final List<Instance> instances = operation.getInstances();


        { // check instance ids
            final Iterator<Instance> iterator = instances.iterator();
            assertEquals("i-90b43b79", iterator.next().getInstanceId());
            assertEquals("i-3bb03fd2", iterator.next().getInstanceId());
            assertEquals("i-91b43b78", iterator.next().getInstanceId());
            assertEquals("i-1eb33cf7", iterator.next().getInstanceId());
            assertEquals("i-eccc4d05", iterator.next().getInstanceId());
            assertEquals("i-efcc4d06", iterator.next().getInstanceId());
            assertEquals("i-42cc4dab", iterator.next().getInstanceId());
            assertEquals("i-32ce4fdb", iterator.next().getInstanceId());
            assertEquals("i-bbce4f52", iterator.next().getInstanceId());
            assertEquals("i-83cf4e6a", iterator.next().getInstanceId());
        }

        { // check instance ids
            final Iterator<Instance> iterator = instances.iterator();
            assertEquals("terminated", iterator.next().getState().getName());
            assertEquals("terminated", iterator.next().getState().getName());
            assertEquals("terminated", iterator.next().getState().getName());
            assertEquals("terminated", iterator.next().getState().getName());
            assertEquals("running", iterator.next().getState().getName());
            assertEquals("running", iterator.next().getState().getName());
            assertEquals("running", iterator.next().getState().getName());
            assertEquals("running", iterator.next().getState().getName());
            assertEquals("running", iterator.next().getState().getName());
            assertEquals("running", iterator.next().getState().getName());
        }

//        instances.forEach(instance -> System.out.printf("assertEquals(\"%s\", iterator.next().getState().getName());%n", instance.getState().getName()));
    }

    @Test
    public void testGetSpotInstanceRequests() throws Exception {
        final AmazonEC2 amazonEC2 = MockEC2Client.fromJson("1437881181099-DescribeSpotInstanceRequestsResult-8210936666888035389.json");

        final Operation operation = new Operation(new OperationId("asdfghjk234567"), amazonEC2);

        final List<SpotInstanceRequest> spotInstanceRequets = operation.getSpotInstanceRequests();

        {
            final Iterator<SpotInstanceRequest> iterator = spotInstanceRequets.iterator();
            assertEquals("sir-02efzkef", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02ep34v9", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02ep7rs7", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02egthbr", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02eh6zfl", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02eldnx3", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02emmkv4", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02ehdb1r", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02ehmelq", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02elghja", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02enh0sr", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02ejzb35", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02ekm3qz", iterator.next().getSpotInstanceRequestId());
            assertEquals("sir-02enlfak", iterator.next().getSpotInstanceRequestId());
        }
        {
            final Iterator<SpotInstanceRequest> iterator = spotInstanceRequets.iterator();
            assertEquals("active", iterator.next().getState());
            assertEquals("active", iterator.next().getState());
            assertEquals("active", iterator.next().getState());
            assertEquals("active", iterator.next().getState());
            assertEquals("active", iterator.next().getState());
            assertEquals("active", iterator.next().getState());
            assertEquals("active", iterator.next().getState());
            assertEquals("closed", iterator.next().getState());
            assertEquals("closed", iterator.next().getState());
            assertEquals("closed", iterator.next().getState());
            assertEquals("closed", iterator.next().getState());
            assertEquals("open", iterator.next().getState());
            assertEquals("open", iterator.next().getState());
            assertEquals("open", iterator.next().getState());
        }

        {
            final Iterator<SpotInstanceRequest> iterator = spotInstanceRequets.iterator();
            assertEquals("i-7d0c83d5", iterator.next().getInstanceId());
            assertEquals("i-42cc4dab", iterator.next().getInstanceId());
            assertEquals("i-bbce4f52", iterator.next().getInstanceId());
            assertEquals("i-efcc4d06", iterator.next().getInstanceId());
            assertEquals("i-eccc4d05", iterator.next().getInstanceId());
            assertEquals("i-83cf4e6a", iterator.next().getInstanceId());
            assertEquals("i-32ce4fdb", iterator.next().getInstanceId());
            assertEquals("i-90b43b79", iterator.next().getInstanceId());
            assertEquals("i-91b43b78", iterator.next().getInstanceId());
            assertEquals("i-1eb33cf7", iterator.next().getInstanceId());
            assertEquals("i-3bb03fd2", iterator.next().getInstanceId());
            assertEquals(null, iterator.next().getInstanceId());
            assertEquals(null, iterator.next().getInstanceId());
            assertEquals(null, iterator.next().getInstanceId());
        }

        {
            final Iterator<SpotInstanceRequest> iterator = spotInstanceRequets.iterator();
            assertEquals(1, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
            assertEquals(2, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
            assertEquals(0, iterator.next().getTags().size());
        }
    }

    @Test
    public void testGetPublicDnsNames() throws Exception {
        final AmazonEC2 amazonEC2 = MockEC2Client.fromJson("1437882646414-DescribeInstancesResult-264183782752925532.json");

        final Operation operation = new Operation(new OperationId("asdfghjk234567"), amazonEC2);

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

//        publicDnsNames.forEach(next -> System.out.printf("assertEquals(\"%s\", iterator.next());%n", next));
    }

    @Test
    public void testGetPrivateDnsNames() throws Exception {
        final AmazonEC2 amazonEC2 = MockEC2Client.fromJson("1437882646414-DescribeInstancesResult-264183782752925532.json");

        final Operation operation = new Operation(new OperationId("asdfghjk234567"), amazonEC2);

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

//        privateDnsNames.forEach(next -> System.out.printf("assertEquals(\"%s\", iterator.next());%n", next));
    }

    @Test
    public void testGetTags() throws Exception {
        final AmazonEC2 amazonEC2 = MockEC2Client.fromJson("1437882646414-DescribeInstancesResult-264183782752925532.json");

        final Operation operation = new Operation(new OperationId("asdfghjk234567"), amazonEC2);

        final List<Tag> tags = operation.getTags();

        {
            final Iterator<Tag> iterator = tags.iterator();
            assertEquals("Name", iterator.next().getKey());
            assertEquals("Name", iterator.next().getKey());
            assertEquals("operation-id", iterator.next().getKey());
            assertEquals("operation-id", iterator.next().getKey());
            assertEquals("operation-id", iterator.next().getKey());
            assertEquals("shutdown", iterator.next().getKey());
            assertEquals("user.name", iterator.next().getKey());
        }

        {
            final Iterator<Tag> iterator = tags.iterator();
            assertEquals("20c401df6e75209b2bcbf6a1ec6949bd172f53d6", iterator.next().getValue());
            assertEquals("null", iterator.next().getValue());
            assertEquals("20c401df6e75209b2bcbf6a1ec6949bd172f53d6", iterator.next().getValue());
            assertEquals("834a27d7cb74cc5e99068bd395bbe9d45a42e871", iterator.next().getValue());
            assertEquals("816058206985f37d95f8b9f091dd35d78f048db5", iterator.next().getValue());
            assertEquals("false", iterator.next().getValue());
            assertEquals("dblevins", iterator.next().getValue());
        }
    }

    @Test
    public void testGetInstanceIds() throws Exception {
        final AmazonEC2 amazonEC2 = MockEC2Client.fromJson("1437882646414-DescribeInstancesResult-264183782752925532.json");

        final Operation operation = new Operation(new OperationId("asdfghjk234567"), amazonEC2);

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

//        instanceIds.forEach(next -> System.out.printf("assertEquals(\"%s\", iterator.next());%n", next));
    }

    @Test
    public void testGetSpotInstanceRequestIds() throws Exception {
        final AmazonEC2 amazonEC2 = MockEC2Client.fromJson("1437881181099-DescribeSpotInstanceRequestsResult-8210936666888035389.json");

        final Operation operation = new Operation(new OperationId("asdfghjk234567"), amazonEC2);

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

//        spotInstanceRequestIds.forEach(next -> System.out.printf("assertEquals(\"%s\", iterator.next());%n", next));
    }


    @Test
    public void testCountInstanceStates() throws Exception {
        final AmazonEC2 amazonEC2 = MockEC2Client.fromJson("1437882646414-DescribeInstancesResult-264183782752925532.json");

        final Operation operation = new Operation(new OperationId("asdfghjk234567"), amazonEC2);

        final Map<String, State> states = operation.countInstanceStates();

        final Iterator<Map.Entry<String, State>> iterator = states.entrySet().iterator();

        {
            final Map.Entry<String, State> entry = iterator.next();
            assertEquals("running", entry.getKey());
            assertEquals("running", entry.getValue().getName());
            assertEquals(6, entry.getValue().getCount());
            assertEquals("running (6)", entry.getValue().toString());
        }
        {
            final Map.Entry<String, State> entry = iterator.next();
            assertEquals("terminated", entry.getKey());
            assertEquals("terminated", entry.getValue().getName());
            assertEquals(4, entry.getValue().getCount());
            assertEquals("terminated (4)", entry.getValue().toString());
        }
    }

    @Test
    public void testCountSpotInstanceRequestStates() throws Exception {

        final AmazonEC2 amazonEC2 = MockEC2Client.fromJson("1437881181099-DescribeSpotInstanceRequestsResult-8210936666888035389.json");

        final Operation operation = new Operation(new OperationId("asdfghjk234567"), amazonEC2);

        final Map<String, State> states = operation.countSpotInstanceRequestStates();
        final Iterator<Map.Entry<String, State>> iterator = states.entrySet().iterator();

        {
            final Map.Entry<String, State> entry = iterator.next();
            assertEquals("active", entry.getKey());
            assertEquals("active", entry.getValue().getName());
            assertEquals(7, entry.getValue().getCount());
            assertEquals("active (7)", entry.getValue().toString());
        }
        {
            final Map.Entry<String, State> entry = iterator.next();
            assertEquals("closed", entry.getKey());
            assertEquals("closed", entry.getValue().getName());
            assertEquals(4, entry.getValue().getCount());
            assertEquals("closed (4)", entry.getValue().toString());
        }
        {
            final Map.Entry<String, State> entry = iterator.next();
            assertEquals("open", entry.getKey());
            assertEquals("open", entry.getValue().getName());
            assertEquals(3, entry.getValue().getCount());
            assertEquals("open (3)", entry.getValue().toString());
        }

    }

}