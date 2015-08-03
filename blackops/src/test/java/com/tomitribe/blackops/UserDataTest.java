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

public class UserDataTest extends Assert {

    @Test
    public void minimum() throws Exception {
        final UserData userData = new UserData("Jane Bond", "6b76b6c83f77fcbdc7cbaec", "ab350c4aabf4c4a3d85affefbdf339c3")
                .script("touch /tmp/foo\n");

        final String actual = toString(userData);

        assertEquals("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n" +
                "function me {(export AWS_ACCESS_KEY=\"6b76b6c83f77fcbdc7cbaec\"; export AWS_SECRET_KEY=\"ab350c4aabf4c4a3d85affefbdf339c3\"; /opt/aws/bin/\"$@\";)}\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"shutdown=false\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"operation-id=op-zh3dd6bdacf7f\"\n" +
                "\n" +
                "cat <<'2dc013eefa7a33ad833c0eb36ba47428' | bash -l\n" +
                "touch /tmp/foo\n" +
                "2dc013eefa7a33ad833c0eb36ba47428\n", actual);
    }

    @Test
    public void withArgs() throws Exception {
        final UserData userData = new UserData("Jane Bond", "6b76b6c83f77fcbdc7cbaec", "ab350c4aabf4c4a3d85affefbdf339c3")
                .script("for n in \"$@\"; do\n" +
                        "    echo \"Arg '$n'\"\n" +
                        "done\n", "one", "two", "three");

        final String actual = toString(userData);

        assertEquals("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n" +
                "function me {(export AWS_ACCESS_KEY=\"6b76b6c83f77fcbdc7cbaec\"; export AWS_SECRET_KEY=\"ab350c4aabf4c4a3d85affefbdf339c3\"; /opt/aws/bin/\"$@\";)}\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"shutdown=false\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"operation-id=op-zh3dd6bdacf7f\"\n" +
                "\n" +
                "cat <<'2dc013eefa7a33ad833c0eb36ba47428' | bash -l -s \"one\" \"two\" \"three\"\n" +
                "for n in \"$@\"; do\n" +
                "    echo \"Arg '$n'\"\n" +
                "done\n" +
                "2dc013eefa7a33ad833c0eb36ba47428\n", actual);
    }

    @Test
    public void withShebangWithArgs() throws Exception {
        final UserData userData = new UserData("Jane Bond", "6b76b6c83f77fcbdc7cbaec", "ab350c4aabf4c4a3d85affefbdf339c3")
                .script("#!/some/path/bash -v -x -l\nfor n in \"$@\"; do\n" +
                        "    echo \"Arg '$n'\"\n" +
                        "done\n", "one", "two", "three");

        final String actual = toString(userData);

        assertEquals("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n" +
                "function me {(export AWS_ACCESS_KEY=\"6b76b6c83f77fcbdc7cbaec\"; export AWS_SECRET_KEY=\"ab350c4aabf4c4a3d85affefbdf339c3\"; /opt/aws/bin/\"$@\";)}\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"shutdown=false\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"operation-id=op-zh3dd6bdacf7f\"\n" +
                "\n" +
                "cat <<'2dc013eefa7a33ad833c0eb36ba47428' | bash -l -s \"one\" \"two\" \"three\"\n" +
                "for n in \"$@\"; do\n" +
                "    echo \"Arg '$n'\"\n" +
                "done\n" +
                "2dc013eefa7a33ad833c0eb36ba47428\n", actual);
    }

    @Test
    public void complete() throws Exception {
        final UserData userData = new UserData("Jane Bond", "83f77f6b76b6ccbdc7cbaec", "ab354aac4a3d85affe0cfbbf4df339c3")
                .tag("Color", "Orange")
                .tag("Shape", "Circle")
                .script("#!/usr/bin/perl -w\nprint \"Hello, Perl\\n\";\n")
                .script("touch /tmp/foo\n")
                .tag("Name", "Jane Bond - Finished")
                .shutdown();


        final String actual = toString(userData);


        assertEquals("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n" +
                "function me {(export AWS_ACCESS_KEY=\"83f77f6b76b6ccbdc7cbaec\"; export AWS_SECRET_KEY=\"ab354aac4a3d85affe0cfbbf4df339c3\"; /opt/aws/bin/\"$@\";)}\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"shutdown=false\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"operation-id=op-zh3dd6bdacf7f\"\n" +
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

    private String toString(UserData userData) {

        // For testing, we need a non-dynamic value
        final String fixedId = "op-zh3dd6bdacf7f";

        // We of course test that the generated ID is unique and dynamic
        assertNotEquals(fixedId, userData.getId());

        // We replace the generated ID with a fixed ID so we can still do a string match
        return userData.toString().replace(userData.getId(), fixedId);
    }

    @Test
    public void shutdownAfter() throws Exception {
        final UserData userData = new UserData("Jane Bond", "83f77f6b76b6ccbdc7cbaec", "ab354aac4a3d85affe0cfbbf4df339c3")
                .shutdownAfter(10, TimeUnit.MINUTES)
                .script("touch /tmp/foo\n");

        final String actual = toString(userData);


        assertEquals("export ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)\n" +
                "function me {(export AWS_ACCESS_KEY=\"83f77f6b76b6ccbdc7cbaec\"; export AWS_SECRET_KEY=\"ab354aac4a3d85affe0cfbbf4df339c3\"; /opt/aws/bin/\"$@\";)}\n" +
                "me ec2-create-tags \"$ID\" --tag \"Name=Jane Bond\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"shutdown=false\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"operation-id=op-zh3dd6bdacf7f\"\n" +
                "me ec2-create-tags \"$ID\" --tag \"shutdown=10m\"\n" +
                "sleep 600 && me ec2-stop-instances \"$ID\" &\n" +
                "\n" +
                "cat <<'2dc013eefa7a33ad833c0eb36ba47428' | bash -l\n" +
                "touch /tmp/foo\n" +
                "2dc013eefa7a33ad833c0eb36ba47428\n", actual);
    }

    @Test
    public void toPipedExecutable() throws Exception {
        {
            final String actual = UserData.toPipedExecutable("" +
                    "date > /tmp/foo.txt");

            final String expected = "" +
                    "bash -l\n" +
                    "date > /tmp/foo.txt";
            assertEquals(expected, actual);
        }

        { // has shebang
            final String actual = UserData.toPipedExecutable("" +
                    "#!/some/path/bash\n" +
                    "date > /tmp/foo.txt");

            final String expected = "" +
                    "/some/path/bash\n" +
                    "date > /tmp/foo.txt";
            assertEquals(expected, actual);
        }

        { // has bash shebang and args
            final String actual = UserData.toPipedExecutable("" +
                    "#!/one/two/bash\n" +
                    "for n in \"$@\"; do echo $n; done", "red", "green", "blue");

            final String expected = "" +
                    "/one/two/bash -s \"red\" \"green\" \"blue\"\n" +
                    "for n in \"$@\"; do echo $n; done";
            assertEquals(expected, actual);
        }


        { // has bash shebang with flags and args
            final String actual = UserData.toPipedExecutable("" +
                    "#!/one/two/bash -q\n" +
                    "for n in \"$@\"; do echo $n; done", "red", "green", "blue");

            final String expected = "" +
                    "/one/two/bash -q -s \"red\" \"green\" \"blue\"\n" +
                    "for n in \"$@\"; do echo $n; done";
            assertEquals(expected, actual);
        }

        { // has sh shebang and args
            final String actual = UserData.toPipedExecutable("" +
                    "#!/foo/the/sh\n" +
                    "for n in \"$@\"; do echo $n; done", "red", "green", "blue");

            final String expected = "" +
                    "/foo/the/sh -s \"red\" \"green\" \"blue\"\n" +
                    "for n in \"$@\"; do echo $n; done";
            assertEquals(expected, actual);
        }

        { // has args and no shebang
            final String actual = UserData.toPipedExecutable("" +
                    "for n in \"$@\"; do echo $n; done", "red", "green", "blue", "orange");

            final String expected = "" +
                    "bash -l -s \"red\" \"green\" \"blue\" \"orange\"\n" +
                    "for n in \"$@\"; do echo $n; done";
            assertEquals(expected, actual);
        }

        try { // args not supported for unknown interpreter
            UserData.toPipedExecutable("#!/foo/notbash\necho \"$@\"", "red", "green", "blue");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }

        try { // args already supplied
            UserData.toPipedExecutable("#!/foo/bash -s \"one\" \"two\" \"three\"\necho \"$@\"", "red", "green", "blue");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    @Test
    public void toArgsString() throws Exception {
        {
            final String actual = UserData.toArgsString("one", "two", "three");
            assertEquals("\"one\" \"two\" \"three\"", actual);
        }

        {
            final String actual = UserData.toArgsString("three");
            assertEquals("\"three\"", actual);
        }

        {
            final String actual = UserData.toArgsString();
            assertEquals("", actual);
        }
    }
}