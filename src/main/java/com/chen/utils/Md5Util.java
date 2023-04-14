package com.chen.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * @Author @Chenxc
 * @Date Hello, 陈杏昌 2023/04/14,014 14:12
 */
public class Md5Util {
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[2048];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 2048)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
            in = null;
            return md5ToString(digest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String md5ToString(byte[] md5Bytes) {
        StringBuilder hexValue = new StringBuilder();
        for (byte b : md5Bytes) {
            int val = ((int) b) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
