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
import org.tomitribe.util.Join;
import org.tomitribe.util.PrintString;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

public class UserData {

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
    private final String name;

    public UserData(final String name, final String accessKey, final String secretKey) {
        this.name = requireNonNull(name, "Name cannot be null");
        requireNonNull(accessKey, "accessKey");
        requireNonNull(secretKey, "secretKey");

        out.print("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n");
        // Command executed in a subshell so subsequent commands cannot read the AWS_SECRET_KEY
        // This includes any Java Processes which may print System.getenv() such as crest-connector demos
        out.printf("function me {(export AWS_ACCESS_KEY=\"%s\"; export AWS_SECRET_KEY=\"%s\"; /opt/aws/bin/\"$@\";)}\n", accessKey, secretKey);
        tag("Name", (name != null) ? name : id.get());
        tag("shutdown", "false");
        tag(id.asTag());

    }

    public String getName() {
        return name;
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

    public UserData script(final URL script, final String... args) throws IOException {
        tag("script", script.toExternalForm());
        try {
            return script(IO.slurp(script), args);
        } finally {
            tag("exit", "$?");
        }
    }

    public UserData script(final File script, final String... args) throws IOException {
        tag("script", script.getName());
        try {
            return script(IO.slurp(script), args);
        } finally {
            tag("exit", "$?");
        }
    }

    public UserData script(final String script, final String... args) throws IOException {
        out.print("\ncat <<'2dc013eefa7a33ad833c0eb36ba47428' | ");

        out.print(toPipedExecutable(script, args));

        out.print("\n2dc013eefa7a33ad833c0eb36ba47428\n");

        return this;
    }

    public static String toPipedExecutable(String script, String... args) {

        // Quote and join the args
        String argsList = toArgsString(args);
        if (argsList.length() != 0) {
            argsList = " -s " + argsList;
        }

        if (script.startsWith("#!")) {

            // chop off the '#!'
            script = script.substring(2);

            // No args to add?
            if (argsList.length() == 0) {
                return script;
            }

            // We have to add the args to the script contents

            // Split off the shebang line from the script
            final int i = script.indexOf('\n');
            String shebang = script.substring(0, i);
            script = script.substring(i);

            // We only support adding args for bash and sh
            if (!shebang.contains("/bash") && !shebang.contains("/sh")) {
                throw new IllegalStateException("Can only add args to bash or sh scripts");
            }

            // No good, there is already a -s arg list
            if (shebang.contains(" -s ")) {
                throw new IllegalStateException(String.format("Script already contains a -s args list: '%s'", shebang));
            }

            return String.format("%s%s%s", shebang, argsList, script);
        } else {
            return String.format("bash -l%s\n%s", argsList, script);
        }
    }

    public static String toArgsString(String... args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = "\"" + args[i] + "\"";
        }

        return Join.join(" ", args);
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
