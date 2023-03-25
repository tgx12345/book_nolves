package com.cduestc.book_novels.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5Utils {
    public static String MD5Upper(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位
            // return buf.toString();
            // 16位
            // return buf.toString().substring(0, 16);

            return buf.toString().substring(0, 20);
        } catch (NoSuchAlgorithmException e) {
           log.error("md5加密失败");
            return null;
        }

    }

}