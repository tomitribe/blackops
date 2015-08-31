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
import com.amazonaws.services.ec2.model.InstanceType;
import com.tomitribe.blackops.OperationBuilder;
import com.tomitribe.blackops.Operations;
import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Default;
import org.tomitribe.crest.api.Option;
import org.tomitribe.crest.api.StreamingOutput;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

@Command
public class Launch {

    private Launch() {
    }

    @Command
    public static StreamingOutput command(@Option("quiet") boolean quiet,
                                          @Option("name") String name,
                                          @Option("shutdown") @Default("false") final boolean shutdown,
                                          @Option("instance-type") @Default("m3.medium") final String instanceType,
                                          @Option("key") @Default("tomitribe_dev") final String keyName,
                                          @Option("security-group") @Default("Ports 60000+10") final String[] securityGroups,
                                          @Option("price") @Default("0.012") final String spotPrice,
                                          @Option("instance-count") @Default("1") final int instanceCount,
                                          @Option("type") @Default("one-time") final String spotRequestType,
                                          @Option("availability-zone") @Default("us-east-1c") final String zone,
                                          @Option("await-capacity") @Default("false") final boolean awaitCapacity,
                                          final String script,
                                          final String[] args
    ) throws IOException {

        final OperationBuilder operationBuilder = new OperationBuilder(name);

        operationBuilder.operation().script(script, args);

        return execute(quiet, shutdown, instanceType, keyName, securityGroups, spotPrice, instanceCount, spotRequestType, zone, awaitCapacity, operationBuilder);
    }

    @Command
    public static StreamingOutput script(@Option("quiet") boolean quiet,
                                         @Option("name") String name,
                                         @Option("shutdown") @Default("false") final boolean shutdown,
                                         @Option("instance-type") @Default("m3.medium") final String instanceType,
                                         @Option("key-name") @Default("tomitribe_dev") final String keyName,
                                         @Option("security-group") @Default("Ports 60000+10") final String[] securityGroups,
                                         @Option("spot-price") @Default("0.012") final String spotPrice,
                                         @Option("instance-count") @Default("1") final int instanceCount,
                                         @Option("spot-request-type") @Default("one-time") final String spotRequestType,
                                         @Option("zone") @Default("us-east-1c") final String zone,
                                         @Option("await-capacity") @Default("false") final boolean awaitCapacity,
                                         final File script,
                                         final String[] args
    ) throws IOException {

        final OperationBuilder operationBuilder = new OperationBuilder((name == null) ? script.getName() : name);

        operationBuilder.operation().script(script, args);

        return execute(quiet, shutdown, instanceType, keyName, securityGroups, spotPrice, instanceCount, spotRequestType, zone, awaitCapacity, operationBuilder);
    }


    public static StreamingOutput execute(
            boolean quiet,
            boolean shutdown, String instanceType, String keyName, String[] securityGroups, String spotPrice,
            int instanceCount, String spotRequestType, String zone, boolean awaitCapacity, OperationBuilder operationBuilder) {
        if (shutdown) {
            operationBuilder.operation().shutdown();
        }

        operationBuilder.specification()
                .withInstanceType(InstanceType.fromValue(instanceType))
                .withKeyName(keyName)
                .withSecurityGroups(securityGroups)
        ;

        operationBuilder.request()
                .withSpotPrice(spotPrice)
                .withInstanceCount(instanceCount)
                .withType(spotRequestType)
                .withAvailabilityZoneGroup(zone);


        final OperationBuilder.Launch launch = operationBuilder.execute();

        return os -> {
            PrintStream stream = new PrintStream(os);
            stream.println(operationBuilder.operation().getId());

            if (quiet) {
                stream.flush();
                stream = new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                    }
                });
            }

            if (!awaitCapacity) {
                stream.printf("%s%n%s%n", operationBuilder.operation().getId(), launch.toString());
                return;
            }

            Operations.awaitFulfillment(stream, launch.getSpotInstancesResult().getSpotInstanceRequests());

            final List<Instance> instances = launch.awaitInstances();

            for (final Instance instance : instances) {
                Instances.print(stream, instance);
            }
        };
    }

}
