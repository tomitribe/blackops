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

import com.tomitribe.blackops.Encryption;
import com.tomitribe.blackops.PEM;
import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Option;
import org.tomitribe.crest.api.StreamingOutput;
import org.tomitribe.util.IO;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Amazon EC2 AMI instances will call the "decrypt" command via cron on @reboot
 */
public class Orders {

    private Orders() {
    }

    @Command
    public static StreamingOutput encrypt(@Option({"public-key", "k"}) final File file) throws Exception {
        final PublicKey key = PEM.readPublicKey(IO.read(file));

        return out -> {
            final byte[] bytes = IO.readBytes(System.in);
            final byte[] encrypt = Encryption.encrypt(bytes, key);
            out.write(encrypt);
            out.flush();
        };
    }

    @Command
    public static StreamingOutput decrypt(@Option({"private-key", "k"}) final File file) throws Exception {
        final PrivateKey key = PEM.readPrivateKey(IO.read(file));

        return out -> {
            final byte[] bytes = IO.readBytes(System.in);
            final byte[] decrypt = Encryption.decrypt(bytes, key);
            out.write(decrypt);
            out.flush();
        };
    }
}
