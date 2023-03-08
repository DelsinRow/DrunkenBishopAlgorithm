package com.orioninc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {
    public static final String CACHE_ALGORITHM = "MD5";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String key = "This is your key";
        System.out.println("Fingerprint: " + fingerprint(keyToMd5(key)));
        System.out.println(Arrays.deepToString(bitsArray(toBinary(keyToMd5(key)))));
    }

    private static byte[] keyToMd5(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(CACHE_ALGORITHM);
        return md5.digest(plainText.getBytes());
    }

    static String toBinary( byte[] bytes ) {
        int count = 0;

        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);

        for( int i = 0; i < Byte.SIZE * bytes.length; i++ ) {
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
            count++;
            if(count % 2 == 0) sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    static String[][] bitsArray(String binaryCode) {
        int count = 0;
        int i = 0;
        int j = 0;
        String [][] binaryArray = new String[16][4];
        for(String str : binaryCode.split(",")) {
            binaryArray[i][j] = str;
            j++;
            count++;
            if(count % 4 == 0) {
                i++;
                j = 0;
            }
        }
        return binaryArray;
    }
    static String fingerprint (byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X ", b));
        }
        return builder.toString();
    }

}