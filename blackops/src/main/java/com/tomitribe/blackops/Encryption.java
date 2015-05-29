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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Encryption {

    public final byte[] data;

    public Encryption(byte[] data) {
        this.data = data;
    }

    public static byte[] decrypt(byte[] message, final PrivateKey privateKey) {

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(message);

        // Read and RSA Decrypt the Key

        final SecretKey secretKey;

        try {
            // Read
            final byte[] encryptedKey = new byte[256];
            final int read = inputStream.read(encryptedKey);
            if (read != encryptedKey.length) throw new IllegalStateException("Cannot read encrypted key");

            // RSA Decrypt
            final Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.DECRYPT_MODE, privateKey);
            final byte[] aesKeyBytes = rsa.doFinal(encryptedKey);

            secretKey = new SecretKeySpec(aesKeyBytes, "AES");

        } catch (Exception e) {

            throw new IllegalStateException("Unable to read and RSA Decrypt the AES SecreteKey", e);
        }

        // Read and AES Decrypt the Payload

        try {
            // Read
            final byte[] encryptedPayload = IO.readBytes(inputStream);

            // AES Decrypt
            final Cipher aes = Cipher.getInstance("AES");
            aes.init(Cipher.DECRYPT_MODE, secretKey);
            return aes.doFinal(encryptedPayload);
        } catch (Exception e) {

            throw new IllegalStateException("Unable to read and AES Decrypt the Payload", e);
        }
    }

    public static byte[] encrypt(final byte[] bytes, final PublicKey publicKey) {

        // 1. Create an AES Secret Key

        final SecretKey secretKey;
        try {
            final KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // for example
            secretKey = keyGen.generateKey();
        } catch (Exception e) {
            throw new IllegalStateException("Uable to generate AES SecretKey", e);
        }

        // 2. RSA Encrypt the AES Secret Key

        final byte[] key;
        try {
            final Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.ENCRYPT_MODE, publicKey);
            key = rsa.doFinal(secretKey.getEncoded());
        } catch (Exception e) {
            throw new IllegalStateException("Uable to RSA Encrypt the AES SecretKey", e);
        }

        // 3. AES Encrypt the Payload

        final byte[] body;
        try {
            final Cipher aes = Cipher.getInstance("AES");
            aes.init(Cipher.ENCRYPT_MODE, secretKey);
            body = aes.doFinal(bytes);
        } catch (Exception e) {
            throw new IllegalStateException("Uable to AES Encrypt the Payload", e);
        }

        // 4. Write the encrypted Key then the Payload

        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream(key.length + body.length);
            stream.write(key);
            stream.write(body);
            stream.close();
            return stream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Uable to write the RSA Encrypted Key and the AES Encrypted the Payload", e);
        }
    }

}
