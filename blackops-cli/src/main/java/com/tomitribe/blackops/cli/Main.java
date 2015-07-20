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
import com.amazonaws.services.ec2.model.SpotInstanceState;
import com.tomitribe.blackops.Encryption;
import com.tomitribe.blackops.Operations;
import com.tomitribe.blackops.Operative;
import com.tomitribe.blackops.PEM;
import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Default;
import org.tomitribe.crest.api.Option;
import org.tomitribe.crest.api.StreamingOutput;
import org.tomitribe.util.IO;
import org.tomitribe.util.Join;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public enum Main {
    ;

    public static void main(String[] args) throws IOException {
        final StreamingOutput output = exec("export > /tmp/foo.txt", "Test", false, "m3.medium", "tomitribe_dev", new String[]{"Ports 60000+10"}, "0.012", 4, "one-time", "us-east-1c", true);
        output.write(System.out);
    }

    @Command
    public static StreamingOutput encrypt(@Option({"public-key", "k"}) final File file) throws Exception {
        final PublicKey key = PEM.readPublicKey(IO.read(file));

        return new StreamingOutput() {

            @Override
            public void write(final OutputStream out) throws IOException {
                final byte[] bytes = IO.readBytes(System.in);
                final byte[] encrypt = Encryption.encrypt(bytes, key);
                out.write(encrypt);
                out.flush();
            }
        };
    }

    @Command
    public static StreamingOutput decrypt(@Option({"private-key", "k"}) final File file) throws Exception {
        final PrivateKey key = PEM.readPrivateKey(IO.read(file));

        return new StreamingOutput() {

            @Override
            public void write(final OutputStream out) throws IOException {
                final byte[] bytes = IO.readBytes(System.in);
                final byte[] decrypt = Encryption.decrypt(bytes, key);
                out.write(decrypt);
                out.flush();
            }
        };
    }


    @Command
    public static StreamingOutput exec(final String script, @Option("name") String name,
                                       @Option("shutdown") @Default("false") final boolean shutdown,
                                       @Option("instance-type") @Default("m3.medium") final String instanceType,
                                       @Option("key-name") @Default("tomitribe_dev") final String keyName,
                                       @Option("security-group") @Default("Ports 60000+10") final String[] securityGroups,
                                       @Option("spot-price") @Default("0.012") final String spotPrice,
                                       @Option("instance-count") @Default("1") final int instanceCount,
                                       @Option("spot-request-type") @Default("one-time") final String spotRequestType,
                                       @Option("zone") @Default("us-east-1c") final String zone,
                                       @Option("await-capacity") @Default("false") final boolean awaitCapacity
    ) throws IOException {

        final Operative operative = new Operative(name);

        operative.operation().script(script);

        return execute(shutdown, instanceType, keyName, securityGroups, spotPrice, instanceCount, spotRequestType, zone, awaitCapacity, operative);
    }

    @Command
    public static StreamingOutput run(final File script, @Option("name") String name,
                                      @Option("shutdown") @Default("false") final boolean shutdown,
                                      @Option("instance-type") @Default("m3.medium") final String instanceType,
                                      @Option("key-name") @Default("tomitribe_dev") final String keyName,
                                      @Option("security-group") @Default("Ports 60000+10") final String[] securityGroups,
                                      @Option("spot-price") @Default("0.012") final String spotPrice,
                                      @Option("instance-count") @Default("1") final int instanceCount,
                                      @Option("spot-request-type") @Default("one-time") final String spotRequestType,
                                      @Option("zone") @Default("us-east-1c") final String zone,
                                      @Option("await-capacity") @Default("false") final boolean awaitCapacity
    ) throws IOException {

        final Operative operative = new Operative((name == null) ? script.getName() : name);

        operative.operation().script(script);

        return execute(shutdown, instanceType, keyName, securityGroups, spotPrice, instanceCount, spotRequestType, zone, awaitCapacity, operative);
    }


    public static StreamingOutput execute(
            boolean shutdown, String instanceType, String keyName, String[] securityGroups, String spotPrice, int instanceCount, String spotRequestType, String zone, boolean awaitCapacity, Operative operative) {
        if (shutdown) {
            operative.operation().shutdown();
        }

        operative.specification()
                .withInstanceType(InstanceType.fromValue(instanceType))
                .withKeyName(keyName)
                .withSecurityGroups(securityGroups)
        ;

        operative.request()
                .withSpotPrice(spotPrice)
                .withInstanceCount(instanceCount)
                .withType(spotRequestType)
                .withAvailabilityZoneGroup(zone);


        final Operative.Launch launch = operative.execute();

        return os -> {
            final PrintStream stream = new PrintStream(os);
            stream.println(operative.operation().getId());

            if (!awaitCapacity) {
                stream.printf("%s%n%s%n", operative.operation().getId(), launch.toString());
                return;
            }

            Operations.awaitFulfillment(launch.getSpotInstancesResult().getSpotInstanceRequests(), stream);

            final List<Instance> instances = launch.awaitInstances();

            instances.forEach(instance -> Instances.print(stream, instance));
        };
    }

}
