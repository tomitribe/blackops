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

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.RequestSpotInstancesResult;
import com.tomitribe.blackops.Aws;
import com.tomitribe.blackops.Operation;
import com.tomitribe.blackops.OperationId;
import com.tomitribe.blackops.Operations;
import com.tomitribe.blackops.State;
import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Default;
import org.tomitribe.crest.api.Err;
import org.tomitribe.crest.api.Exit;
import org.tomitribe.crest.api.Option;
import org.tomitribe.crest.api.Out;
import org.tomitribe.crest.api.StreamingOutput;
import org.tomitribe.util.Duration;
import org.tomitribe.util.Join;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Command("operation")
public class OperationCli {

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


    @Command
    public void awaitCapacity(
            @Err PrintStream err,
            @Out PrintStream out,
            @Option("retry") @Default("5 seconds") final Duration retry,
            @Option("timeout") @Default("1 hour") final Duration timeout,
            final OperationId operationId, final int capacity) throws TimeoutException {

        final Operation operation = new Operation(operationId, Aws.client());

        try {
            final List<Instance> instances = operation.awaitCapacity(capacity,
                    retry.getTime(), retry.getUnit(),
                    timeout.getTime(), timeout.getUnit(),
                    (s) -> err.print("\r" + s)
            );

            err.println();

            instances.forEach(instance -> out.printf("%-45s %-10s %-12s %-12s %-17s%n",
                            instance.getPublicDnsName(),
                            instance.getState().getName(),
                            instance.getInstanceType(),
                            instance.getInstanceId(),
                            instance.getKeyName()
                    )
            );
        } catch (IllegalStateException e) {
            err.println();
            throw new OperationException(e.getMessage());
        } catch (TimeoutException e) {
            err.println();
            throw new OperationException("Timeout: Giving up");
        }
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

    //    @Command
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
            try {
                Operations.awaitFulfillment(stream, result.getSpotInstanceRequests());
            } catch (TimeoutException e) {
                throw new IllegalStateException(e);
            }
        };
    }

    @Exit(-1)
    public static class OperationException extends RuntimeException {
        public OperationException(String message) {
            super(message);
        }
    }
}
