package com.identity.arx.general;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESencrp {
    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[]{(byte) 84, (byte) 104, (byte) 101, (byte) 66, (byte) 101, (byte) 115, (byte) 116, (byte) 83, (byte) 101, (byte) 99, (byte) 114, (byte) 101, (byte) 116, (byte) 75, (byte) 101, (byte) 121};

    public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(1, key);
        return null;
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(2, key);
        return null;
    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGO);
    }
}
