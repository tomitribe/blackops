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

import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeSpotInstanceRequestsResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.SpotInstanceRequest;
import com.amazonaws.services.ec2.model.SpotInstanceState;
import com.amazonaws.services.ec2.model.Tag;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.tomitribe.blackops.Assertions.assertInstance;
import static com.tomitribe.blackops.Assertions.assertState;

public class AwsTest extends Assert {

    @Test
    public void testGetInstances() throws Exception {
        final DescribeInstancesResult o = (DescribeInstancesResult) MockEC2Client.load("1437880318802-DescribeInstancesResult-8872862273707674404.json");
        final List<Instance> instances = Aws.getInstances(o);
        Assertions.generateInstanceAssert(instances);

        final Iterator<Instance> iterator = instances.iterator();
        assertInstance("i-90b43b79", "sir-02ehdb1r", "running", "ec2-54-90-47-95.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-3bb03fd2", "sir-02enh0sr", "running", "ec2-54-237-108-135.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-91b43b78", "sir-02ehmelq", "running", "ec2-54-237-42-116.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-1eb33cf7", "sir-02elghja", "running", "ec2-54-237-80-181.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-eccc4d05", "sir-02eh6zfl", "running", "ec2-54-237-110-193.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-efcc4d06", "sir-02egthbr", "running", "ec2-54-90-58-137.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-42cc4dab", "sir-02ep34v9", "running", "ec2-54-237-26-16.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-32ce4fdb", "sir-02emmkv4", "running", "ec2-54-237-8-49.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-bbce4f52", "sir-02ep7rs7", "running", "ec2-54-237-69-184.compute-1.amazonaws.com", iterator.next());
        assertInstance("i-83cf4e6a", "sir-02eldnx3", "running", "ec2-54-157-138-7.compute-1.amazonaws.com", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetName() throws Exception {
        {
            final String name = Aws.getName(new Instance().withTags(new Tag("Name", "Orange")));
            assertEquals("Orange", name);
        }

        { // The name tag is case-sensitive
            assertNull(Aws.getName(new Instance().withTags(new Tag("name", "Orange"))));
        }
    }

    /**
     * Returned list should tolerate duplicates and maintain insertion order
     *
     * The test intentionally has duplicates and items not in alphabetical order
     */
    @Test
    public void testGetSpotInstanceStates() throws Exception {
        final List<SpotInstanceState> states = Aws.getSpotInstanceStates(new DescribeSpotInstanceRequestsResult().withSpotInstanceRequests(
                Arrays.asList(
                        new SpotInstanceRequest().withState(SpotInstanceState.Failed),
                        new SpotInstanceRequest().withState(SpotInstanceState.Open),
                        new SpotInstanceRequest().withState(SpotInstanceState.Open),
                        new SpotInstanceRequest().withState(SpotInstanceState.Cancelled),
                        new SpotInstanceRequest().withState(SpotInstanceState.Active),
                        new SpotInstanceRequest().withState(SpotInstanceState.Cancelled),
                        new SpotInstanceRequest().withState(SpotInstanceState.Active)
                )
        ));

        final Iterator<SpotInstanceState> iterator = states.iterator();
        assertEquals(SpotInstanceState.Failed, iterator.next());
        assertEquals(SpotInstanceState.Open, iterator.next());
        assertEquals(SpotInstanceState.Open, iterator.next());
        assertEquals(SpotInstanceState.Cancelled, iterator.next());
        assertEquals(SpotInstanceState.Active, iterator.next());
        assertEquals(SpotInstanceState.Cancelled, iterator.next());
        assertEquals(SpotInstanceState.Active, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Returned list should tolerate duplicates and maintain insertion order
     *
     * The test intentionally has duplicates and items not in alphabetical order
     */
    @Test
    public void testGetSpotInstanceStates1() throws Exception {
        final List<SpotInstanceState> states = Aws.getSpotInstanceStates(Arrays.asList(
                new SpotInstanceRequest().withState(SpotInstanceState.Cancelled),
                new SpotInstanceRequest().withState(SpotInstanceState.Open),
                new SpotInstanceRequest().withState(SpotInstanceState.Open),
                new SpotInstanceRequest().withState(SpotInstanceState.Cancelled),
                new SpotInstanceRequest().withState(SpotInstanceState.Cancelled),
                new SpotInstanceRequest().withState(SpotInstanceState.Active)
        ));

        final Iterator<SpotInstanceState> iterator = states.iterator();
        assertEquals(SpotInstanceState.Cancelled, iterator.next());
        assertEquals(SpotInstanceState.Open, iterator.next());
        assertEquals(SpotInstanceState.Open, iterator.next());
        assertEquals(SpotInstanceState.Cancelled, iterator.next());
        assertEquals(SpotInstanceState.Cancelled, iterator.next());
        assertEquals(SpotInstanceState.Active, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Returned list should tolerate duplicates and maintain insertion order
     *
     * The test intentionally has duplicates and items not in alphabetical order
     */
    @Test
    public void testGetSpotInstanceRequestIds() throws Exception {
        final List<String> ids = Aws.getSpotInstanceRequestIds(Arrays.asList(
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02eh6zfl"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02elb8ae"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02ekm3qz"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02ehmelq"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02eg705h"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02ehdb1r"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02elb8ae"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02ehmelq"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02elghja"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02eldnx3"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02egthbr"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02egthbr"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02efzkef"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02ejzb35"),
                new SpotInstanceRequest().withSpotInstanceRequestId("sir-02ehmelq")
        ));

        final Iterator<String> iterator = ids.iterator();
        assertEquals("sir-02eh6zfl", iterator.next());
        assertEquals("sir-02elb8ae", iterator.next());
        assertEquals("sir-02ekm3qz", iterator.next());
        assertEquals("sir-02ehmelq", iterator.next());
        assertEquals("sir-02eg705h", iterator.next());
        assertEquals("sir-02ehdb1r", iterator.next());
        assertEquals("sir-02elb8ae", iterator.next());
        assertEquals("sir-02ehmelq", iterator.next());
        assertEquals("sir-02elghja", iterator.next());
        assertEquals("sir-02eldnx3", iterator.next());
        assertEquals("sir-02egthbr", iterator.next());
        assertEquals("sir-02egthbr", iterator.next());
        assertEquals("sir-02efzkef", iterator.next());
        assertEquals("sir-02ejzb35", iterator.next());
        assertEquals("sir-02ehmelq", iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Returned list should tolerate duplicates and maintain insertion order
     *
     * The test intentionally has duplicates and items not in alphabetical order
     */
    @Test
    public void testGetInstanceIds() throws Exception {
        final List<String> ids = Aws.getInstanceIds(Arrays.asList(
                new Instance().withInstanceId("i-a6149b4f"),
                new Instance().withInstanceId("i-a6149b4f"),
                new Instance().withInstanceId("i-a6149b4f"),
                new Instance().withInstanceId("i-62159a8b"),
                new Instance().withInstanceId("i-59d91a30"),
                new Instance().withInstanceId("i-32a159ca"),
                new Instance().withInstanceId("i-9a614b4f"),
                new Instance().withInstanceId("i-a8b62159"),
                new Instance().withInstanceId("i-d9159a30"),
                new Instance().withInstanceId("i-z31a59ca")
        ));

        final Iterator<String> iterator = ids.iterator();
        assertEquals("i-a6149b4f", iterator.next());
        assertEquals("i-a6149b4f", iterator.next());
        assertEquals("i-a6149b4f", iterator.next());
        assertEquals("i-62159a8b", iterator.next());
        assertEquals("i-59d91a30", iterator.next());
        assertEquals("i-32a159ca", iterator.next());
        assertEquals("i-9a614b4f", iterator.next());
        assertEquals("i-a8b62159", iterator.next());
        assertEquals("i-d9159a30", iterator.next());
        assertEquals("i-z31a59ca", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCountInstanceStates() throws Exception {
        final Map<String, State> states = Aws.countInstanceStates(Arrays.asList(
                        new Instance().withState(new InstanceState().withName(InstanceStateName.Running)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.Stopping)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.Running)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.ShuttingDown)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.Running)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.Pending)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.Running)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.Stopped)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.ShuttingDown)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.Running)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.Terminated)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.ShuttingDown)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.Pending)),
                        new Instance().withState(new InstanceState().withName(InstanceStateName.Terminated)))
        );

        final Iterator<Map.Entry<String, State>> iterator = states.entrySet().iterator();
        assertState("pending", 2, iterator.next());
        assertState("running", 5, iterator.next());
        assertState("shutting-down", 3, iterator.next());
        assertState("stopped", 1, iterator.next());
        assertState("stopping", 1, iterator.next());
        assertState("terminated", 2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * States should be in the map in alphabetical order
     */
    @Test
    public void testCountSpotInstanceRequestStates() throws Exception {
        final Map<String, State> states = Aws.countSpotInstanceRequestStates(Arrays.asList(
                new SpotInstanceRequest().withState(SpotInstanceState.Cancelled),
                new SpotInstanceRequest().withState(SpotInstanceState.Open),
                new SpotInstanceRequest().withState(SpotInstanceState.Open),
                new SpotInstanceRequest().withState(SpotInstanceState.Cancelled),
                new SpotInstanceRequest().withState(SpotInstanceState.Cancelled),
                new SpotInstanceRequest().withState(SpotInstanceState.Active)
        ));

        final Iterator<Map.Entry<String, State>> iterator = states.entrySet().iterator();
        assertState("active", 1, iterator.next());
        assertState("cancelled", 3, iterator.next());
        assertState("open", 2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAsFilter() throws Exception {
        {
            final Filter filter = Aws.asFilter(SpotInstanceState.Active);
            Assertions.assertFilter(filter, "state", "active");
        }

        {
            final Filter filter = Aws.asFilter(SpotInstanceState.Closed, SpotInstanceState.Cancelled);
            Assertions.assertFilter(filter, "state", "closed", "cancelled");
        }

        {
            final Filter filter = Aws.asFilter(SpotInstanceState.values());
            Assertions.assertFilter(filter, "state",
                    "open", "active", "closed", "cancelled", "failed"
            );
        }
    }

    @Test
    public void testAsFilter1() throws Exception {

        {
            final Filter filter = Aws.asFilter(InstanceStateName.Running);
            Assertions.assertFilter(filter, "instance-state-name", "running");
        }

        {
            final Filter filter = Aws.asFilter(InstanceStateName.Pending);
            Assertions.assertFilter(filter, "instance-state-name", "pending");
        }

        {
            final Filter filter = Aws.asFilter(InstanceStateName.values());
            Assertions.assertFilter(filter, "instance-state-name",
                    "pending", "running", "shutting-down", "terminated", "stopping", "stopped"
            );
        }
    }

}