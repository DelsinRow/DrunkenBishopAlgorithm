package com.orioninc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String key = "This is your key";
        System.out.println(Arrays.toString(keyToMd5(key)));
    }

    private static byte[] keyToMd5(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return md5.digest(plainText.getBytes());
    }

    static String fingerprint (byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X ", b));
        }


        return builder.toString();
    }

}