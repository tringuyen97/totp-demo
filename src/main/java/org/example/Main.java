package org.example;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

/**
 * Creator: Nguyen Ngoc Tri
 * Date: 9/9/2024
 * Time: 5:24 PM
 */
public class Main {

    private static final int s = 30000;
    private static final String K = "nguyenngoctri";

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {

        System.out.println("Start generate totp...");

        long time = new Date().getTime();
        System.out.println("Time: " + time);
        long timeWindow = time / s;
        System.out.println("Time window: " + timeWindow);

        byte[] HS = hmacSHA1(longToBytes(timeWindow), K.getBytes());

        System.out.println("HS: " + Arrays.toString(HS));

        int offset = HS[19] & 0xf;
        System.out.println("offset: " + offset);

        int bin_code = (HS[offset] & 0x7f) << 24
                | (HS[offset + 1] & 0xff) << 16
                | (HS[offset + 2] & 0xff) << 8
                | (HS[offset + 3] & 0xff);
        System.out.println("bin_code: " + bin_code);

        int otp = bin_code % 1000000;
        System.out.println(otp);


    }

    public static byte[] hmacSHA1(byte[] data, byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKeySpec);
        return mac.doFinal(data);
    }

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return result;
    }

}