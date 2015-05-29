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

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class OperationTest extends Assert {

    @Test
    public void minimum() throws Exception {
        final String actual = new Operation("Jane Bond", "6b76b6c83f77fcbdc7cbaec", "ab350c4aabf4c4a3d85affefbdf339c3")
                .script("touch /tmp/foo\n")
                .toString();

        assertEquals("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n" +
                "function me {(export AWS_ACCESS_KEY=\"%s\"; export AWS_SECRET_KEY=\"%s\"; /opt/aws/bin/\"$@\";)}\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond\"\n" +
                "\n" +
                "cat <<'2dc013eefa7a33ad833c0eb36ba47428' | bash -l\n" +
                "touch /tmp/foo\n" +
                "2dc013eefa7a33ad833c0eb36ba47428\n", actual);
    }

    @Test
    public void complete() throws Exception {
        final String actual = new Operation("Jane Bond", "83f77f6b76b6ccbdc7cbaec", "ab354aac4a3d85affe0cfbbf4df339c3")
                .tag("Color", "Orange")
                .tag("Shape", "Circle")
                .script("#!/usr/bin/perl -w\nprint \"Hello, Perl\\n\";\n")
                .script("touch /tmp/foo\n")
                .tag("Name", "Jane Bond - Finished")
                .shutdown()
                .toString();


        assertEquals("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n" +
                "function me {(export AWS_ACCESS_KEY=\"%s\"; export AWS_SECRET_KEY=\"%s\"; /opt/aws/bin/\"$@\";)}\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"Color=Orange\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"Shape=Circle\"\n" +
                "\n" +
                "cat <<'2dc013eefa7a33ad833c0eb36ba47428' | /usr/bin/perl -w\n" +
                "print \"Hello, Perl\\n\";\n" +
                "2dc013eefa7a33ad833c0eb36ba47428\n" +
                "\n" +
                "cat <<'2dc013eefa7a33ad833c0eb36ba47428' | bash -l\n" +
                "touch /tmp/foo\n" +
                "2dc013eefa7a33ad833c0eb36ba47428\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond - Finished\"\n" +
                "me ec2-stop-instances \"$ID\"\n", actual);
    }

    @Test
    public void shutdownAfter() throws Exception {
        final String actual = new Operation("Jane Bond", "83f77f6b76b6ccbdc7cbaec", "ab354aac4a3d85affe0cfbbf4df339c3")
                .script("touch /tmp/foo\n")
                .shutdownAfter(10, TimeUnit.MINUTES)
                .toString();


        assertEquals("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n" +
                "function me {(export AWS_ACCESS_KEY=\"%s\"; export AWS_SECRET_KEY=\"%s\"; /opt/aws/bin/\"$@\";)}\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond\"\n" +
                "\n" +
                "cat <<'2dc013eefa7a33ad833c0eb36ba47428' | bash -l\n" +
                "touch /tmp/foo\n" +
                "2dc013eefa7a33ad833c0eb36ba47428\n" +
                "sleep 600 && me ec2-stop-instances \"$ID\" &\n", actual);
    }

}