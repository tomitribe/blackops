package com.tomitribe.blackops;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.tomitribe.util.IO;

import java.io.ByteArrayInputStream;
import java.net.URL;

public class EncryptionTest extends Assert {

    private static final String PUBLIC_PEM = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6yDZK9RNLi8bh7N1X9zZ\n" +
            "e5s/BUQbrc8qMU0sHWjaAxmAThndFFmzgUcmyNlKskphpFVLP4QQw2CNp/XiP0do\n" +
            "22dvRIeSTx+hBg6NlcSHUqROzC+Un1m8QaEZoWVdgcZOF7tGe77rrhDLPprkyAZM\n" +
            "O9KYEZzwu09hO0lHDDxBUAn4MktykCWawiK58yzbBgxwDtoq5rWXSs+j0Z0tqwTS\n" +
            "+npCOpnaGpLqCcau3XJ54rzoUsOv6N8wb0t8E77z7zCr5oD8tvBzBvyyo4h9Fwud\n" +
            "/w2Xbli9b1YpR81si6mNFJprE4FzZR8z+2yvVWsKdOLtZL38AwAQnwM8NBfjt2sB\n" +
            "UwIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";
    private static final String PRIVATE_PEM = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEowIBAAKCAQEA6yDZK9RNLi8bh7N1X9zZe5s/BUQbrc8qMU0sHWjaAxmAThnd\n" +
            "FFmzgUcmyNlKskphpFVLP4QQw2CNp/XiP0do22dvRIeSTx+hBg6NlcSHUqROzC+U\n" +
            "n1m8QaEZoWVdgcZOF7tGe77rrhDLPprkyAZMO9KYEZzwu09hO0lHDDxBUAn4Mkty\n" +
            "kCWawiK58yzbBgxwDtoq5rWXSs+j0Z0tqwTS+npCOpnaGpLqCcau3XJ54rzoUsOv\n" +
            "6N8wb0t8E77z7zCr5oD8tvBzBvyyo4h9Fwud/w2Xbli9b1YpR81si6mNFJprE4Fz\n" +
            "ZR8z+2yvVWsKdOLtZL38AwAQnwM8NBfjt2sBUwIDAQABAoIBADnUl3WPPvhn/7Zt\n" +
            "riXI1Pjw4xwxdzlVpcQAfiQxoMV4ZTVQaS7TLTJTxLdLsnKNltxrWhmiKtlTCyRP\n" +
            "drknoCOumhBZ4+NwHRO3Sp2We84YuYTBAyX6VTIzJHGs08XSd2yg1CIlgCQM32Bj\n" +
            "inekDOX90wcp+Y+oY3rIeCALRekgQeg4U7NsNe78X01wTbLTEAoTvfhFiIuS9mvy\n" +
            "gDiIyq6ao5C4WMTJfGFTwhbFVQCgMhHGT8q3QW1yMUCKDaZblspKhg5p9QcG7Sbr\n" +
            "0TLMN9vRzst3oZpIMlYSIpVJQt/734QPaMygWoEeyIDMw5F30OpnaAmXn4d7i2D6\n" +
            "yMWOrUkCgYEA+FmoIYsH1J4kEltbbqYqZYjJrwZPIVcoWHt6pif2U+oyvXM1kaNz\n" +
            "mQcOPwdv7YoTOL2PF5X4IenJ1n3ipBqqI+yuPKR8di0Q0L2NuGCFMwMTbh0npObu\n" +
            "p5w7vDZBSkIUVaWvNXOF1TmawlE/Lm5FFNuQCUF5TwQx0ctXmg5gaNUCgYEA8l7u\n" +
            "cGQDDhUXukHbD7vHgTDckLafdrZfhi4lQiOrfDr2+p3gvdid/NbyoyhmARAcfCRT\n" +
            "rsfGzGDTSNLXl4fDvNEY3P51I9t+lJqKh36VHTXK4x1o+6k0HmuKsL1nfwLC0u7A\n" +
            "ZJmECeiZWD3RfFmHnvn3rEGef2xcw/VyM1MxVYcCgYB7TD/dz2Gm3JB6EKM3LwYT\n" +
            "fLLs00wL+HKOY1eEEIkKJlbkmcM5uBWXHtqizjVykD90xx+4jPsxExnjBIK2CIr5\n" +
            "5PKoJt2n6j3T9JfctwbKSbemzpC1HPXVfwplKeaGUII591UiS2VhdATyflXI3nQr\n" +
            "o8pB3iC06xc02ih20rZotQKBgHrAabKydHmlZScIQ5JsYFamEI+abpkJNMgYhvZA\n" +
            "apG4q22ymCiOlTWBB9Jqm37eUg7ttxRGSgEo6NlxL4+nSxNOM3F5zooimLFas957\n" +
            "1bm9jMwKESmkQKlHfi6VPt2s4E9EbFyHA8Cg9Ukjxf1curthjnbHpSaUE0eMABLO\n" +
            "h6XxAoGBAOp1zcrrOj2Tfn0YwalcrbpG/lf856moaEkYttDiGP2m4KW9CIpzKHOK\n" +
            "kpewGKDilLY0y+I/yec4KyD2dSOFeZFlHKnN0V5sojBZ3xw4wCWtIQhBZCa6YC4V\n" +
            "VBVsihI+F1OWopbD8Qhr83CrCpxxqrf8xR6gmne5WSFVmganVRqj\n" +
            "-----END RSA PRIVATE KEY-----\n";

    @Test
    public void test() throws Exception {

        final URL resource = this.getClass().getClassLoader().getResource("data.txt");
        final String expected = IO.slurp(resource);

        final byte[] encrypted = Encryption.encrypt(expected.getBytes(), PEM.readPublicKey(new ByteArrayInputStream(PUBLIC_PEM.getBytes())));

        assertNotEquals(expected, new String(encrypted, "UTF-8"));

        final byte[] decrypted = Encryption.decrypt(encrypted, PEM.readPrivateKey(new ByteArrayInputStream(PRIVATE_PEM.getBytes())));

        assertEquals(expected, new String(decrypted, "UTF-8"));
    }


    @Test(expected = IllegalStateException.class)
    public void badKey() throws Exception {

        final URL resource = this.getClass().getClassLoader().getResource("data.txt");
        final String expected = IO.slurp(resource);

        final byte[] encrypted = Encryption.encrypt(expected.getBytes(), PEM.readPublicKey(new ByteArrayInputStream(PUBLIC_PEM.getBytes())));

        assertNotEquals(expected, new String(encrypted, "UTF-8"));

        // Mess up the data
        encrypted[10] = (byte) (encrypted[10] - 1);

        final byte[] decrypted = Encryption.decrypt(encrypted, PEM.readPrivateKey(new ByteArrayInputStream(PRIVATE_PEM.getBytes())));

        assertEquals(expected, new String(decrypted, "UTF-8"));
    }

    @Ignore
    @Test(expected = IllegalStateException.class)
    public void badContent() throws Exception {

        final URL resource = this.getClass().getClassLoader().getResource("data.txt");
        final String expected = IO.slurp(resource);

        final byte[] encrypted = Encryption.encrypt(expected.getBytes(), PEM.readPublicKey(new ByteArrayInputStream(PUBLIC_PEM.getBytes())));

        assertNotEquals(expected, new String(encrypted, "UTF-8"));

        // Mess up the data
        encrypted[1000] = (byte) (encrypted[1000] - 1);

        final byte[] decrypted = Encryption.decrypt(encrypted, PEM.readPrivateKey(new ByteArrayInputStream(PRIVATE_PEM.getBytes())));

        assertEquals(expected, new String(decrypted, "UTF-8"));
    }

}