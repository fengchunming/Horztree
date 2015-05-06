package com.an.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Digest {
    public enum Cipher {
        HEX, BASE64
    }

    public static byte[] MD5(String source) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String MD5(String source, Cipher cip) {
        if (cip == Cipher.HEX) {
            return Digest.byte2Hex(Digest.MD5(source));
        } else {
            return Digest.byte2Base64(Digest.MD5(source));
        }
    }

    public static byte[] SHA1(String source) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(source.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String SHA1(String source, Cipher cip) {
        if (cip == Cipher.HEX) {
            return Digest.byte2Hex(Digest.SHA1(source));
        } else {
            return Digest.byte2Base64(Digest.SHA1(source));
        }
    }


    public static String byte2Hex(byte[] buffer) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (byte x : buffer) {
            sb.append(Character.forDigit((x & 240) >> 4, 16));
            sb.append(Character.forDigit(x & 15, 16));
        }
        return sb.toString();
    }

    public static String byte2Base64(byte[] buffer) {
        return Base64.encodeBase64String(buffer);
    }


    public static String hex2Base64(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toLowerCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return Base64.encodeBase64String(d);
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }

}
