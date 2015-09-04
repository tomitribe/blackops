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
import org.tomitribe.util.editor.AbstractConverter;
import org.tomitribe.util.editor.Editors;

import java.beans.PropertyEditorManager;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Command
public class Launch {

    static {
        PropertyEditorManager.registerEditor(InstanceType.class, InstanceTypeEditor.class);
        Editors.get(InstanceType.class);
    }

    private Launch() {
    }

    @Command
    public static StreamingOutput command(@Option("quiet") boolean quiet,
                                          @Option("name") String name,
                                          @Option("shutdown") @Default("false") final boolean shutdown,
                                          @Option("key") @Default("tomitribe_dev") final String keyName,
                                          @Option("security-group") @Default("Ports 60000+10") final String[] securityGroups,
                                          @Option("price") @Default("0.012") final String spotPrice,
                                          @Option("instance-count") @Default("1") final int instanceCount,
                                          @Option("instance-type") @Default("m3.medium") final String instanceType,
                                          @Option("type") @Default("one-time") final String spotRequestType,
                                          @Option("availability-zone") @Default("us-east-1c") final String zone,
                                          @Option("await-capacity") @Default("false") final boolean awaitCapacity,
                                          final String script
    ) throws IOException {

        final OperationBuilder operationBuilder = new OperationBuilder(name);

        operationBuilder.operation().script(script);

        return execute(quiet, shutdown, keyName, securityGroups, spotPrice,
                instanceCount, spotRequestType, zone, awaitCapacity, operationBuilder,
                InstanceType.fromValue(instanceType));
    }

    @Command
    public static StreamingOutput script(@Option("quiet") boolean quiet,
                                         @Option("name") String name,
                                         @Option("shutdown") @Default("false") final boolean shutdown,
                                         @Option("key-name") @Default("tomitribe_dev") final String keyName,
                                         @Option("security-group") @Default("Ports 60000+10") final String[] securityGroups,
                                         @Option("spot-price") @Default("0.012") final String spotPrice,
                                         @Option("instance-count") @Default("1") final int instanceCount,
                                         @Option("instance-type") @Default("m3.medium") final String instanceType,
                                         @Option("spot-request-type") @Default("one-time") final String spotRequestType,
                                         @Option("zone") @Default("us-east-1c") final String zone,
                                         @Option("await-capacity") @Default("false") final boolean awaitCapacity,
                                         final File script
    ) throws IOException {

        final OperationBuilder operationBuilder = new OperationBuilder((name == null) ? script.getName() : name);

        operationBuilder.operation().script(script);

        return execute(quiet, shutdown, keyName, securityGroups, spotPrice,
                instanceCount, spotRequestType, zone, awaitCapacity, operationBuilder,
                InstanceType.fromValue(instanceType));
    }


    public static StreamingOutput execute(
            boolean quiet,
            boolean shutdown, String keyName, String[] securityGroups, String spotPrice,
            int instanceCount, String spotRequestType, String zone, boolean awaitCapacity, OperationBuilder operationBuilder, final InstanceType instanceType1) {
        if (shutdown) {
            operationBuilder.operation().shutdown();
        }

        operationBuilder.specification()
                .withInstanceType(instanceType1)
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

            try {
                Operations.awaitFulfillment(stream, launch.getSpotInstancesResult().getSpotInstanceRequests());
            } catch (TimeoutException e) {
                throw new IllegalStateException(e);
            }

            final List<Instance> instances;
            try {
                instances = launch.awaitInstances();
            } catch (TimeoutException e) {
                throw new IllegalStateException(e);
            }

            for (final Instance instance : instances) {
                Instances.print(stream, instance);
            }
        };
    }

    public static class InstanceTypeEditor extends AbstractConverter {
        @Override
        protected Object toObjectImpl(String s) {
            return InstanceType.fromValue(s);
        }
    }
}
