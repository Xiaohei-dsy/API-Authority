package com.aiit.authority.utils;

import com.aiit.authority.BaseTest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class JsonUtilsTest extends BaseTest {

    @Test
    public void fromJsonTest() {
        UserInfo userDO = getUser();
        String userJson = userJson();
        Assertions.assertEquals(JsonUtils.fromJson(userJson, UserInfo.class), userDO);
    }

    @Test
    public void isJson() {
        Assertions.assertTrue(JsonUtils.isJson(userJson()));
        String ss = getUser().toString();
        Assertions.assertFalse(JsonUtils.isJson(ss));
    }


    @Test
    public void toJsonTest() {
        UserInfo userDO = getUser();
        Assertions.assertTrue(JsonUtils.isJson(JsonUtils.toJson(userDO)));
    }


    private UserInfo getUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUsername("name");
        userInfo.setPassword("123456");
        userInfo.setRole(0);
        userInfo.setRemarkName("张三");
        userInfo.setStatus(0);
        return userInfo;
    }

    private String userJson() {
        return "{\n" +
                "  \"id\" : 1,\n" +
                "  \"username\" : \"name\",\n" +
                "  \"password\" : \"123456\",\n" +
                "  \"remarkName\" : \"张三\",\n" +
                "  \"role\" : 0,\n" +
                "  \"status\" : 0\n" +
                "}";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class UserInfo {

        private Long id;

        private String username;

        private String password;

        private String remarkName;

        private Integer role;

        private Integer status;
    }
}
