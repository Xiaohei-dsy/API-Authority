package com.aiit.authority.utils;

import org.springframework.stereotype.Component;

@Component
public class ValidUtils {
    /**
     * 通过正则表达式验证参数
     */
    public static Boolean isValid(String str, String regex) {
        return str.matches(regex);
    }

    /**
     * 用于匹配由数字、26个英文字母或者下划线组成的字符串。
     */
    public static Boolean isValidWord(String str) {
        String regexStr = "^\\w+$";
        return str.matches(regexStr);
    }

    /**
     * 增加长度验证，同时匹配由数字、26个英文字母或者下划线组成的指定长度字符串。
     */
    public static Boolean isValidField(String str, Integer length) {
        String regexStr = "^\\w+$";
        return str.matches(regexStr) && (str.length() <= length);
    }

    /**
     * 匹配由中文、数字、26个英文字母或者下划线组成的指定长度字符串，不能以下划线开头或结尾。
     */
    public static Boolean isValidName(String str, Integer length) {
        String regexStr = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$";
        return str.matches(regexStr) && (str.length() <= length);
    }

    /**
     * 匹配由数字、26个英文字母或者下划线组成的指定长度字符串。
     */
    public static Boolean isValidPassword(String str, Integer maxLength, Integer minLength) {
        String regexStr = "^\\w+$";
        return str.matches(regexStr) && (str.length() <= maxLength) && (str.length() >= minLength);
    }

}
