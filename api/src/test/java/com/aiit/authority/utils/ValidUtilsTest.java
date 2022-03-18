package com.aiit.authority.utils;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import com.aiit.authority.BaseTest;

public class ValidUtilsTest extends BaseTest {

    @Test
    public void isValidTest() {
        String strValid="AbC_123";
        String strInValid="*@ABC_123";
        //匹配字母-数字-下划线
        String regexStr="^\\w+$";
        Assertions.assertTrue(ValidUtils.isValid(strValid, regexStr));
        Assertions.assertFalse(ValidUtils.isValid(strInValid, regexStr));
    }

    @Test
    public void isValidWordTest() {
        String strValid="AbC_123";
        String strInValid="*@AbC_123";
        Assertions.assertTrue(ValidUtils.isValidWord(strValid));
        Assertions.assertFalse(ValidUtils.isValidWord(strInValid));
    }

    @Test
    public void isValidFieldTest() {
        String strValid="AbC_123";
        String strTypeInValid="+@AbC_123";
        String strLenInValid="AbCDEFG_123456789";
        Assertions.assertTrue(ValidUtils.isValidField(strValid,10));
        Assertions.assertFalse(ValidUtils.isValidField(strTypeInValid,10));
        Assertions.assertFalse(ValidUtils.isValidField(strLenInValid,10));
    }

    @Test
    public void isValidNameTest() {
        String strValid="中文_AbC_123";
        String strTypeInValid="+@AbC_123";
        String strNameInValid="_中文AbC_123";
        String strLenInValid="中文AbCDEFG_123456789";
        Assertions.assertTrue(ValidUtils.isValidName(strValid,10));
        Assertions.assertFalse(ValidUtils.isValidName(strTypeInValid,10));
        Assertions.assertFalse(ValidUtils.isValidName(strNameInValid,10));
        Assertions.assertFalse(ValidUtils.isValidName(strLenInValid,10));
    }

    @Test
    public void isValidPasswordTest(){
        String strValid="AbC_123";
        String strTypeInValid="AbC 123";
        String strLenInValid1="AbCDEFG_123456789nckjhad";
        String strLenInValid2="123";
        Assertions.assertTrue(ValidUtils.isValidPassword(strValid,15,6));
        Assertions.assertFalse(ValidUtils.isValidPassword(strTypeInValid,15,6));
        Assertions.assertFalse(ValidUtils.isValidPassword(strLenInValid1,15,6));
        Assertions.assertFalse(ValidUtils.isValidPassword(strLenInValid2,15,6));
    }

}