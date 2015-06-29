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

import com.amazonaws.services.ec2.model.InstanceType;
import com.tomitribe.blackops.Encryption;
import com.tomitribe.blackops.Operative;
import com.tomitribe.blackops.PEM;
import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Default;
import org.tomitribe.crest.api.Option;
import org.tomitribe.crest.api.StreamingOutput;
import org.tomitribe.util.IO;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

public enum Main {
    ;

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
    public static String exec(final String script, @Option("name") String name,
                              @Option("shutdown") @Default("false") final boolean shutdown,
                              @Option("instance-type") @Default("m3.medium") final String instanceType,
                              @Option("key-name") @Default("tomitribe_dev") final String keyName,
                              @Option("security-group") @Default("Ports 60000+10") final String securityGroup,
                              @Option("spot-price") @Default("0.012") final String spotPrice,
                              @Option("instance-count") @Default("1") final int instanceCount,
                              @Option("spot-request-type") @Default("one-time") final String spotRequestType,
                              @Option("zone") @Default("us-east-1c") final String zone
    ) throws IOException {

        final Operative operative = new Operative(name);

        operative.operation().script(script);

        if (shutdown) {
            operative.operation().shutdown();
        }

        operative.specification()
                .withInstanceType(InstanceType.fromValue(instanceType))
                .withKeyName(keyName)
                .withSecurityGroups(securityGroup)
        ;

        operative.request()
                .withSpotPrice(spotPrice)
                .withInstanceCount(instanceCount)
                .withType(spotRequestType)
                .withAvailabilityZoneGroup(zone);


        return String.format("%s%n%s", operative.operation().getId(), operative.execute().toString());
    }

    @Command
    public static String run(final File script, @Option("name") String name,
                             @Option("shutdown") @Default("false") final boolean shutdown,
                             @Option("instance-type") @Default("m3.medium") final String instanceType,
                             @Option("key-name") @Default("tomitribe_dev") final String keyName,
                             @Option("security-group") @Default("Ports 60000+10") final String[] securityGroups,
                             @Option("spot-price") @Default("0.012") final String spotPrice,
                             @Option("instance-count") @Default("1") final int instanceCount,
                             @Option("spot-request-type") @Default("one-time") final String spotRequestType,
                             @Option("zone") @Default("us-east-1c") final String zone
    ) throws IOException {

        final Operative operative = new Operative((name == null) ? script.getName() : name);

        operative.operation().script(script);

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


        return String.format("%s%n%s", operative.operation().getId(), operative.execute().toString());
    }

}
