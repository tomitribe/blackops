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
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public enum Encryption {
    ;

    public static byte[] decrypt(byte[] message, final PrivateKey privateKey) {

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(message);

        final byte[] key = new byte[256];
        final byte[] signature = new byte[32];
        final byte[] body;

        readExactly(inputStream, key, "Cannot read RSA encrypted key");
        readExactly(inputStream, signature, "Cannot read AES signature");
        try {
            body = IO.readBytes(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot read AES Encrypted body", e);
        }

        // 1. RSA Decrypt the Key

        final SecretKey secretKey;

        try {

            final Cipher rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.DECRYPT_MODE, privateKey);
            final byte[] decrypted = rsa.doFinal(key);

            secretKey = new SecretKeySpec(decrypted, "AES");

        } catch (Exception e) {
            throw new IllegalStateException("Unable to RSA Decrypt the AES SecreteKey", e);
        }


        // 2. Check the Signature of the Body

        try {
            final Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            final byte[] expected = mac.doFinal(body);
            final String a = new String(signature, "UTF-8");
            final String b = new String(expected, "UTF-8");
            if (!a.equals(b)) {
                throw new IllegalStateException("Signature validation failed: Signatures do not match");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to validate Signature", e);
        }

        // 3. Read and AES Decrypt the Payload

        try {

            // AES Decrypt
            final Cipher aes = Cipher.getInstance("AES");
            aes.init(Cipher.DECRYPT_MODE, secretKey);
            final byte[] bytes = aes.doFinal(body);

            return gunzip(bytes);

        } catch (Exception e) {
            throw new IllegalStateException("Unable to AES Decrypt the Payload", e);
        }
    }

    private static void readExactly(final ByteArrayInputStream inputStream, final byte[] required, final String message) {
        try {

            final int i = inputStream.read(required);

            if (i != required.length) {
                throw new IllegalStateException(message);
            }

        } catch (IOException e) {
            throw new IllegalStateException(message, e);
        }
    }

    public static byte[] encrypt(final byte[] bytes, final PublicKey publicKey) {
        final byte[] gzip = gzip(bytes);

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
            body = aes.doFinal(gzip);
        } catch (Exception e) {
            throw new IllegalStateException("Uable to AES Encrypt the Payload", e);
        }

        // 4. Sign the Encrypted Payload

        final byte[] signature;
        try {
            final Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            signature = mac.doFinal(body);
        } catch (Exception e) {
            throw new IllegalStateException("Uable to Sign the Payload", e);
        }

        try {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream(key.length + body.length);
            stream.write(key); // 265 bytes
            stream.write(signature); // 32 bytes
            stream.write(body); // n bytes
            stream.close();
            return stream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Uable to write the RSA Encrypted Key and the AES Encrypted the Payload", e);
        }
    }


    private static byte[] gzip(byte[] data) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);

            gzipOutputStream.write(data);
            gzipOutputStream.close();

            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to gzip the operation script", e);
        }
    }

    private static byte[] gunzip(byte[] data) {
        try {
            final ByteArrayInputStream baos = new ByteArrayInputStream(data);
            final GZIPInputStream inputStream = new GZIPInputStream(baos);
            return IO.readBytes(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to gzip the operation script", e);
        }
    }

}
