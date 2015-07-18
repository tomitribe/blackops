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
        final Operation operation = new Operation("Jane Bond", "6b76b6c83f77fcbdc7cbaec", "ab350c4aabf4c4a3d85affefbdf339c3")
                .script("touch /tmp/foo\n");

        final String actual = toString(operation);

        assertEquals("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n" +
                "function me {(export AWS_ACCESS_KEY=\"6b76b6c83f77fcbdc7cbaec\"; export AWS_SECRET_KEY=\"ab350c4aabf4c4a3d85affefbdf339c3\"; /opt/aws/bin/\"$@\";)}\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"shutdown=false\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"operation-id=2778495aef93cb95b663dd6bdacf7f466e09466d\"\n" +
                "\n" +
                "cat <<'2dc013eefa7a33ad833c0eb36ba47428' | bash -l\n" +
                "touch /tmp/foo\n" +
                "2dc013eefa7a33ad833c0eb36ba47428\n", actual);
    }

    @Test
    public void complete() throws Exception {
        final Operation operation = new Operation("Jane Bond", "83f77f6b76b6ccbdc7cbaec", "ab354aac4a3d85affe0cfbbf4df339c3")
                .tag("Color", "Orange")
                .tag("Shape", "Circle")
                .script("#!/usr/bin/perl -w\nprint \"Hello, Perl\\n\";\n")
                .script("touch /tmp/foo\n")
                .tag("Name", "Jane Bond - Finished")
                .shutdown();


        final String actual = toString(operation);


        assertEquals("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n" +
                "function me {(export AWS_ACCESS_KEY=\"83f77f6b76b6ccbdc7cbaec\"; export AWS_SECRET_KEY=\"ab354aac4a3d85affe0cfbbf4df339c3\"; /opt/aws/bin/\"$@\";)}\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"shutdown=false\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"operation-id=2778495aef93cb95b663dd6bdacf7f466e09466d\"\n" +
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
                "me ec2-create-tags \"$ID\" --tag \"shutdown=true\"\n" +
                "me ec2-stop-instances \"$ID\"\n", actual);
    }

    private String toString(Operation operation) {

        // For testing, we need a non-dynamic value
        final String fixedId = "2778495aef93cb95b663dd6bdacf7f466e09466d";

        // We of course test that the generated ID is unique and dynamic
        assertNotEquals(fixedId, operation.getId());

        // We replace the generated ID with a fixed ID so we can still do a string match
        return operation.toString().replace(operation.getId(), fixedId);
    }

    @Test
    public void shutdownAfter() throws Exception {
        final Operation operation = new Operation("Jane Bond", "83f77f6b76b6ccbdc7cbaec", "ab354aac4a3d85affe0cfbbf4df339c3")
                .shutdownAfter(10, TimeUnit.MINUTES)
                .script("touch /tmp/foo\n")
                ;

        final String actual = toString(operation);


        assertEquals("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n" +
                "function me {(export AWS_ACCESS_KEY=\"83f77f6b76b6ccbdc7cbaec\"; export AWS_SECRET_KEY=\"ab354aac4a3d85affe0cfbbf4df339c3\"; /opt/aws/bin/\"$@\";)}\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"shutdown=false\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"operation-id=2778495aef93cb95b663dd6bdacf7f466e09466d\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"shutdown=10m\"\n" +
                "sleep 600 && me ec2-stop-instances \"$ID\" &\n" +
                "\n" +
                "cat <<'2dc013eefa7a33ad833c0eb36ba47428' | bash -l\n" +
                "touch /tmp/foo\n" +
                "2dc013eefa7a33ad833c0eb36ba47428\n", actual);
    }

}