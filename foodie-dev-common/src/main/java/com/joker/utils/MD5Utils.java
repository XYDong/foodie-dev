package com.joker.utils;


import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
/**
 * @version 1.0.0
 * @ClassName MD5Utils.java
 * @Package com.joker.utils
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月26日 10:21:00
 */
public class MD5Utils {
    /**
     *
     * @Title: MD5Utils.java
     * @Package com.imooc.utils
     * @Description: 对字符串进行md5加密
     */
    public static String getMD5Str(String strValue) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String newstr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
        return newstr;
    }
}
