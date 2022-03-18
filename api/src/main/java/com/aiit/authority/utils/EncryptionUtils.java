package com.aiit.authority.utils;

import org.apache.commons.codec.digest.DigestUtils;


public class EncryptionUtils {

    /**
     * 根据随机salt和pwd生成加密值
     *
     * @param password
     * @return
     */
    public static String getCipher(String password) {
        // 加密公式：md5(sha1(pwd)+salt)
        return DigestUtils.md5Hex(password);
    }


}
