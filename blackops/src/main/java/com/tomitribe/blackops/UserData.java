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

import org.tomitribe.util.IO;
import org.tomitribe.util.Longs;
import org.tomitribe.util.PrintString;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class UserData {
    // TODO Make Name required
    // TODO Allow scripts to have args

    public static final String PUBLIC_PEM = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA9C7Fi7EJAgvxU7PybJNP\n" +
            "jlQua8JdBpVdV9Bs0wN+K9yyOUqdnjvIddkD1AD8/hg88I4Jlt2HBayOz4JT3T2y\n" +
            "vFDc8MnraunbI6gRL8kPhZrVo0WOdOXytBXYK5g3EpI3VGipAfAfDP20otH13Nia\n" +
            "ks3fV3O1j6/YXCjxYML9Ui/7BqDcq4dKvfKBqvkql2EmA4hosgdHWEmVavIFzYS0\n" +
            "05FSFZklvo5hXo0YL3tno9Aey2LztAibF37jvfIzYKiW86L3gFfAsY5vvd1AgvzI\n" +
            "+umBIfKCYDpndQFgBA8Kc9zdfSOCEtwDDBfxiNbllQcO/STTuNcOa92UhMl5gIui\n" +
            "0QIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";

    private final PrintString out = new PrintString();
    private final OperationId id = OperationId.generate();

    public UserData(final String name, final String accessKey, final String secretKey) {
        out.print("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n");
        // Command executed in a subshell so subsequent commands cannot read the AWS_SECRET_KEY
        // This includes any Java Processes which may print System.getenv() such as crest-connector demos
        out.printf("function me {(export AWS_ACCESS_KEY=\"%s\"; export AWS_SECRET_KEY=\"%s\"; /opt/aws/bin/\"$@\";)}\n", accessKey, secretKey);
        tag("Name", (name != null) ? name : id.get());
        tag("shutdown", "false");
        tag(id.asTag());

    }

    public String getId() {
        return id.get();
    }

    public OperationId getOperationId() {
        return id;
    }

    public UserData tag(final String name, final String value) {
        out.printf("me ec2-create-tags \"$ID\" --tag \"%s=%s\"\n", name, value);
        return this;
    }

    public UserData tag(com.amazonaws.services.ec2.model.Tag tag) {
        return tag(tag.getKey(), tag.getValue());
    }

    /**
     * Shutsdown the VM immediately
     */
    public UserData shutdown() {
        tag("shutdown", "true");
        // Command executed in a subshell so subsequent commands cannot read the AWS_SECRET_KEY
        // This includes any Java Processes which may print System.getenv() such as crest-connector demos
        out.printf("me ec2-stop-instances \"$ID\"\n");
        return this;
    }

    /**
     * Shutsdown the VM immediately
     */
    public UserData shutdownAfter(final long time, final TimeUnit unit) {
        final long seconds = unit.toSeconds(time);

        tag("shutdown", TimeUnit.SECONDS.toMinutes(seconds) + "m");
        out.printf("sleep %s && me ec2-stop-instances \"$ID\" &\n", seconds);
        return this;
    }

    public UserData script(final URL script) throws IOException {
        tag("script", script.toExternalForm());
        try {
            return script(IO.readBytes(script));
        } finally {
            tag("exit", "$?");
        }
    }

    public UserData script(final File script) throws IOException {
        tag("script", script.getName());
        try {
            return script(IO.readBytes(script));
        } finally {
            tag("exit", "$?");
        }
    }

    public UserData script(final String script) throws IOException {
        return script(script.getBytes());
    }

    public UserData script(final byte[] script) throws IOException {
        out.print("\ncat <<'2dc013eefa7a33ad833c0eb36ba47428' | ");

        int length = script.length;

        if (script[script.length - 1] == '\n') {
            length--;
        }

        // Use the interpreter of the script
        if ('#' == script[0] && '!' == script[1]) {
            out.write(script, 2, length - 2);
        } else {
            // Use Bash
            out.print("bash -l\n");
            out.write(script, 0, length);
        }

        out.print("\n2dc013eefa7a33ad833c0eb36ba47428\n");

        return this;
    }

    public String toUserData() {
        try {
            final byte[] data = out.toByteArray();

            final PublicKey publicKey = PEM.readPublicKey(new ByteArrayInputStream(PUBLIC_PEM.getBytes()));

            final byte[] encrypt = Encryption.encrypt(data, publicKey);

            return new String(base64(encrypt), "UTF-8");
        } catch (Exception e) {
            throw new IllegalStateException("Unable to UTF-8 encode the final operation script string", e);
        }
    }

    private static byte[] base64(byte[] bytes) {
        return Base64.encodeBase64(bytes);
    }


    @Override
    public String toString() {
        return out.toString();
    }
}
