package com.orioninc;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {
    private static final String CACHE_ALGORITHM_MD5 = "MD5";
    static int iPos = 4;
    static int jPos = 8;

    public static void main(String[] args) throws NoSuchAlgorithmException {
//        String myString = "{\"key\" : \"Some text\"}";
//        JSONObject job = new JSONObject(myString);
        System.out.print("Please, enter the key: ");
        String key = inputSomething();
//        System.out.println(job.get("key"));
        String fingerprint = fingerprint(keyToMd5(key));
        System.out.println("\nYou entered: " + key);
        printFingerPrint(fingerprint);
//        System.out.println("Bits array: " + Arrays.deepToString(bitsArray(toBinary(keyToMd5(key)))));
        int[][] array = createField(bitsArray(toBinary(keyToMd5(key))));
        printCaptureArray(graphicKey(array));

    }

    private static String inputSomething() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        try {
            input = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    private static byte[] keyToMd5(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(CACHE_ALGORITHM_MD5);
        return md5.digest(plainText.getBytes());
    }

    private static String toBinary(byte[] bytes) {
        int count = 0;
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++) {
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
            count++;
            if (count % 2 == 0) sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private static String[][] bitsArray(String binaryCode) {
        int count = 0;
        int i = 0;
        int j = 0;
        String[][] binaryArray = new String[16][4];
        for (String str : binaryCode.split(",")) {
            binaryArray[i][j] = str;
            j++;
            count++;
            if (count % 4 == 0) {
                i++;
                j = 0;
            }
        }
        return binaryArray;
    }

    private static String fingerprint(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X ", b));
        }
        return builder.toString();
    }

    private static int[][] createField(String[][] bitsArray) {
        int[][] fieldArray = new int[9][17];
        for (int[] a : fieldArray) {
            for (int b : a) {
                b = 0;
            }
        }

        for (String[] fingerByte : bitsArray) {
            for (int k = fingerByte.length - 1; k >= 0; k--) {
                switch (fingerByte[k]) {
                    case "00" -> moveLeftUp(fieldArray);
                    case "01" -> moveRightUp(fieldArray);
                    case "10" -> moveLeftDown(fieldArray);
                    case "11" -> moveRightDown(fieldArray);
                }
                fieldArray[iPos][jPos]++;
            }
        }
        return fieldArray;
    }

    private static void moveLeftUp(int[][] captureArray) { //T == b, L == c, a
        if (iPos == 0) {
            if (jPos == 0) ;                     //a
            else jPos--;                        //T == b
        } else if (jPos == 0) iPos--;           //c == L
        else {
            iPos--;
            jPos--;
        }
    }

    private static void moveRightUp(int[][] captureArray) { //T == a, R == d, b
        if (iPos == 0) {
            if (jPos == 16) ;                    //b
            else jPos++;                        //a == T
        } else if (jPos == 16) iPos--;          //d == R
        else {
            iPos--;
            jPos++;
        }
    }

    private static void moveLeftDown(int[][] captureArray) { // B==d, L==a,c
        if (iPos == 8) {
            if (jPos == 0) ;                     //c
            else jPos--;                        //d == B
        } else if (jPos == 0) iPos++;           //a == L
        else {
            iPos++;
            jPos--;
        }
    }

    private static void moveRightDown(int[][] captureArray) { //B==c, R==b, d
        if (iPos == 8) {
            if (jPos == 16) ;                    //d
            else jPos++;                        //B == c
        } else if (jPos == 16) iPos++;           //b == R
    }

    private static char[][] graphicKey(int[][] intArray) {
        char[][] graphicKey = new char[9][17];
        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray[i].length; j++) {
                switch (intArray[i][j]) {
                    case 0 -> graphicKey[i][j] = ' ';
                    case 1 -> graphicKey[i][j] = '.';
                    case 2 -> graphicKey[i][j] = 'o';
                    case 3 -> graphicKey[i][j] = '+';
                    case 4 -> graphicKey[i][j] = '=';
                    case 5 -> graphicKey[i][j] = '*';
                    case 6 -> graphicKey[i][j] = 'B';
                    case 7 -> graphicKey[i][j] = 'O';
                    case 8 -> graphicKey[i][j] = 'X';
                    case 9 -> graphicKey[i][j] = '@';
                    case 10 -> graphicKey[i][j] = '%';
                    case 11 -> graphicKey[i][j] = '&';
                    case 12 -> graphicKey[i][j] = '#';
                    case 13 -> graphicKey[i][j] = '/';
                    case 14 -> graphicKey[i][j] = '^';
                }
            }
        }
        graphicKey[4][8] = 'S';
        graphicKey[iPos][jPos] = 'E';

        return graphicKey;
    }

    private static void printCaptureArray(char[][] array) {
        System.out.println("+-----RSA Key-----+");
        for (char[] a : array) {
            System.out.print("|");
            for (int i = 0; i < a.length; i++) {
                System.out.print(a[i]);
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("+-----------------+");
    }

    private static void printFingerPrint(String fingerprint) {
        String fb = fingerprint.replace(' ', ':');
        System.out.println("Fingerprint: " + fb.substring(0, fb.length() - 1));
    }

}