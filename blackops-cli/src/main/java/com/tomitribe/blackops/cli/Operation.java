/*
 * Tomitribe Confidential
 *
 * Copyright Tomitribe Corporation. 2015
 *
 * The source code for this program is not published or otherwise divested 
 * of its trade secrets, irrespective of what has been deposited with the 
 * U.S. Copyright Office.
 */
package com.tomitribe.blackops.cli;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.RequestSpotInstancesResult;
import com.tomitribe.blackops.Aws;
import com.tomitribe.blackops.OperationId;
import com.tomitribe.blackops.Operations;
import com.tomitribe.blackops.State;
import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.StreamingOutput;
import org.tomitribe.util.Join;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

@Command
public class Operation {

    @Command
    public void terminate(final OperationId operationId) {
        Operations.terminateOperation(operationId);
    }

    @Command
    public String capacity(final OperationId operationId) {
        final List<Instance> instances = Operations.getInstances(operationId);

        final Map<String, State> count = Aws.countInstanceStates(instances);

        return Join.join(", ", count.values());
    }

    public static void main(String[] args) throws IOException {

        final Operation operation = new Operation();
        final StreamingOutput expand =
                operation.expand(new OperationId("13cc97275e0a9c75a4c60d01024d3faee3850635"), 20);

        expand.write(System.out);
    }

    @Command
    public StreamingOutput instances(final OperationId operationId) {
        final List<Instance> instances = Operations.getInstances(operationId);
        return Instances.listInstances(instances);
    }

    @Command
    public StreamingOutput hosts(final OperationId operationId) {
        final List<Instance> instances = Operations.getInstances(operationId);
        return outputStream -> {
            final PrintStream out = new PrintStream(outputStream);
            instances.forEach(instance -> out.println(instance.getPublicDnsName()));
        };
    }

    @Command
    public StreamingOutput expand(final OperationId operationId, final int to) {
        final List<Instance> instances = Operations.getInstances(operationId);

        final Map<String, State> count = Aws.countInstanceStates(instances);

        final State state = count.get(InstanceStateName.Running.name().toLowerCase());
        final int running = state != null ? state.getCount() : 0;

        final RequestSpotInstancesResult result = Operations.expandOperation(operationId, to - running);

        System.out.println(result);
        return awaitFulfillment(result);
    }

    public static StreamingOutput awaitFulfillment(final RequestSpotInstancesResult result) {
        return os -> {
            final PrintStream stream = new PrintStream(os);
            Operations.awaitFulfillment(stream, result.getSpotInstanceRequests());
        };
    }
}
