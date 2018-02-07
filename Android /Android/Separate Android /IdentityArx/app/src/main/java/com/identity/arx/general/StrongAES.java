package com.identity.arx.general;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class StrongAES {
    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[]{(byte) 84, (byte) 104, (byte) 101, (byte) 66, (byte) 101, (byte) 115, (byte) 116, (byte) 83, (byte) 101, (byte) 99, (byte) 114, (byte) 101, (byte) 116, (byte) 75, (byte) 101, (byte) 121};
    String enc;

    public static String encrypt(String Data) throws Exception {
        String text = Data;
        try {
            System.out.println(text);
            Key aesKey = generateKey();
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(1, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : encrypted) {
                sb.append((char) b);
            }
            return sb.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key aesKey = generateKey();
        byte[] bb = new byte[encryptedData.length()];
        for (int i = 0; i < encryptedData.length(); i++) {
            bb[i] = (byte) encryptedData.charAt(i);
        }
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(2, aesKey);
        return new String(cipher.doFinal(bb));
    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGO);
    }
}
