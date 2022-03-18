package com.aiit.authority.service;

import com.aiit.authority.BaseTest;
import com.aiit.authority.controller.request.ActivateUserRequest;
import com.aiit.authority.controller.request.AdminLoginRequest;
import com.aiit.authority.controller.request.AdminLogoutRequest;
import com.aiit.authority.controller.request.AdminRegisterRequest;
import com.aiit.authority.controller.request.AdminRegisterUserRequest;
import com.aiit.authority.controller.request.DeleteUserRequest;
import com.aiit.authority.controller.request.DisableUserRequest;
import com.aiit.authority.controller.request.ListAllUsersRequest;
import com.aiit.authority.controller.request.ListPendingUsersRequest;
import com.aiit.authority.controller.request.ListReviewedUsersRequest;
import com.aiit.authority.controller.request.QueryUserExistRequest;
import com.aiit.authority.controller.request.UserLoginRequest;
import com.aiit.authority.controller.request.UserLogoutRequest;
import com.aiit.authority.controller.request.UserRegisterRequest;
import com.aiit.authority.controller.response.AdminLoginResponse;
import com.aiit.authority.controller.response.AdminLogoutResponse;
import com.aiit.authority.controller.response.ListAllUsersResponse;
import com.aiit.authority.controller.response.ListApprovedUsersResponse;
import com.aiit.authority.controller.response.ListPendingUsersResponse;
import com.aiit.authority.controller.response.QueryUserExistResponse;
import com.aiit.authority.controller.response.UserLoginResponse;
import com.aiit.authority.controller.response.UserLogoutResponse;
import com.aiit.authority.controller.response.vo.UserInfoVO;
import com.aiit.authority.enums.RedisPreFixEnum;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.enums.UserPositionEnum;
import com.aiit.authority.enums.UserStatusEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.aiit.authority.manager.UserManager;
import com.aiit.authority.repository.entity.UserDO;
import com.aiit.authority.repository.redis.RedisUtils;
import com.aiit.authority.utils.EncryptionUtils;
import com.aiit.authority.utils.ThreadLocalUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ManageServiceTest extends BaseTest {


    @Resource
    ManageService manageService;

    @Resource
    UserManager userManager;

    @Resource
    RedisUtils redisUtils;

    @BeforeEach
    void init(){
        // 用于适配日志模块从ThreadLocal拿username的需求
        ThreadLocalUtil.set("username","super_admin");
    }

    @Test
    @Transactional
    @Rollback
    void registerUser() {

        // 注册一个用户
        addUser("user1", "123456", "用户1");

        // 验证用户信息在数据库的正确性
        UserDO user1 = userManager.getUser("user1");
        assertEquals("user1", user1.getUsername());
        assertEquals(EncryptionUtils.getCipher("123456"), user1.getPassword());
        assertEquals("用户1", user1.getRemarkName());
        assertEquals(UserPositionEnum.NORMAL_USER.getValue(), user1.getIsManager());
        assertEquals(UserStatusEnum.WAITING_APPROVAL.getValue(), user1.getStatus());

    }

    @Test
    @Transactional
    @Rollback
    void registerAdmin() {

        // 注册一个管理员
        addAdmin("admin", "123456", "管理员");

        // 验证管理员信息在数据库的正确性
        UserDO admin = userManager.getUser("admin");
        assertEquals("admin", admin.getUsername());
        assertEquals(EncryptionUtils.getCipher("123456"), admin.getPassword());
        assertEquals("管理员", admin.getRemarkName());
        assertEquals(UserPositionEnum.ADMIN.getValue(), admin.getIsManager());
        assertEquals(UserStatusEnum.ACTIVE_USER.getValue(), admin.getStatus());

    }

    @Test
    @Transactional
    @Rollback
    void listAllUsers() {

        // 插入1个管理员2个普通用户
        addAdmin("admin", "123456", "管理员");
        addUser("user1", "123456", "用户1");
        addUser("user2", "123456", "用户2");

        // 模拟请求，获取响应中的列表
        ListAllUsersRequest request = new ListAllUsersRequest();
        request.setCurrentPage(1);
        request.setPageSize(10);
        ListAllUsersResponse response = manageService.listAllUsers(request);

        // 验证列表长度
        List<UserInfoVO> userInfoVOList = response.getUserInfoList();
        assertEquals(3, userInfoVOList.size());

        // 验证列表各项
        assertEquals("admin", userInfoVOList.get(0).getUsername());
        assertEquals("管理员", userInfoVOList.get(0).getRemarkName());
        assertEquals(UserPositionEnum.ADMIN.getValue(), userInfoVOList.get(0).getIsManager());
        assertEquals(UserStatusEnum.ACTIVE_USER.getValue(), userInfoVOList.get(0).getStatus());

        assertEquals("user1", userInfoVOList.get(1).getUsername());
        assertEquals("用户1", userInfoVOList.get(1).getRemarkName());
        assertEquals(UserPositionEnum.NORMAL_USER.getValue(), userInfoVOList.get(1).getIsManager());
        assertEquals(UserStatusEnum.WAITING_APPROVAL.getValue(), userInfoVOList.get(1).getStatus());

        assertEquals("user2", userInfoVOList.get(2).getUsername());
        assertEquals("用户2", userInfoVOList.get(2).getRemarkName());
        assertEquals(UserPositionEnum.NORMAL_USER.getValue(), userInfoVOList.get(2).getIsManager());
        assertEquals(UserStatusEnum.WAITING_APPROVAL.getValue(), userInfoVOList.get(2).getStatus());
    }


    @Test
    @Transactional
    @Rollback
    void listPendingUsers() {

        // 插入1个管理员2个普通用户
        addAdmin("admin", "123456", "管理员");
        addUser("user1", "123456", "用户1");
        addUser("user2", "123456", "用户2");

        // 模拟请求，获取响应中的列表
        ListPendingUsersRequest request = new ListPendingUsersRequest();
        request.setCurrentPage(1);
        request.setPageSize(10);
        ListPendingUsersResponse response = manageService.listPendingUsers(request);

        // 验证列表长度
        List<UserInfoVO> userInfoVOList = response.getUserInfoList();
        assertEquals(2, userInfoVOList.size());

        // 验证列表各项
        assertEquals("user1", userInfoVOList.get(0).getUsername());
        assertEquals("用户1", userInfoVOList.get(0).getRemarkName());
        assertEquals(UserPositionEnum.NORMAL_USER.getValue(), userInfoVOList.get(0).getIsManager());
        assertEquals(UserStatusEnum.WAITING_APPROVAL.getValue(), userInfoVOList.get(0).getStatus());

        assertEquals("user2", userInfoVOList.get(1).getUsername());
        assertEquals("用户2", userInfoVOList.get(1).getRemarkName());
        assertEquals(UserPositionEnum.NORMAL_USER.getValue(), userInfoVOList.get(1).getIsManager());
        assertEquals(UserStatusEnum.WAITING_APPROVAL.getValue(), userInfoVOList.get(1).getStatus());
    }

    @Test
    @Transactional
    @Rollback
    void listReviewedUsers() {

        // 插入1个管理员2个普通用户
        addAdmin("admin", "123456", "管理员");
        addUser("user1", "123456", "用户1");
        addUser("user2", "123456", "用户2");
        addUser("user3", "123456", "用户3");


        // 模拟请求，获取响应中的列表
        ListReviewedUsersRequest request = new ListReviewedUsersRequest();
        request.setCurrentPage(1);
        request.setPageSize(10);
        ListApprovedUsersResponse response = manageService.listReviewedUsers(request);

        // 验证列表长度
        List<UserInfoVO> userInfoVOList = response.getUserInfoList();
        assertEquals(0, userInfoVOList.size());

        // 启用user1
        ActivateUserRequest request2 = new ActivateUserRequest();
        request2.setUsername("user1");
        manageService.activateUser(request2);

        // 禁用user2
        DisableUserRequest request3 = new DisableUserRequest();
        request3.setUsername("user2");
        manageService.disableUser(request3);

        // 验证列表长度
        response = manageService.listReviewedUsers(request);
        userInfoVOList = response.getUserInfoList();
        assertEquals(2, userInfoVOList.size());

        // 验证列表各项
        assertEquals("user1", userInfoVOList.get(0).getUsername());
        assertEquals("用户1", userInfoVOList.get(0).getRemarkName());
        assertEquals(UserPositionEnum.NORMAL_USER.getValue(), userInfoVOList.get(0).getIsManager());
        assertEquals(UserStatusEnum.ACTIVE_USER.getValue(), userInfoVOList.get(0).getStatus());

        assertEquals("user2", userInfoVOList.get(1).getUsername());
        assertEquals("用户2", userInfoVOList.get(1).getRemarkName());
        assertEquals(UserPositionEnum.NORMAL_USER.getValue(), userInfoVOList.get(1).getIsManager());
        assertEquals(UserStatusEnum.BANNED_USER.getValue(), userInfoVOList.get(1).getStatus());
    }

    @Test
    @Transactional
    @Rollback
    void activateUser() {

        // 插入1个普通用户
        addUser("user1", "123456", "用户1");

        // 尝试更新用户状态
        ActivateUserRequest request2 = new ActivateUserRequest();
        request2.setUsername("user1");
        manageService.activateUser(request2);

        // 验证用户信息在数据库的正确性
        UserDO user1 = userManager.getUser("user1");
        assertEquals(UserStatusEnum.ACTIVE_USER.getValue(), user1.getStatus());

    }

    @Test
    @Transactional
    @Rollback
    void registerUserByAdmin() {

        // 插入一个管理员
        addAdmin("admin", "123456", "管理员");

        // 管理员注册一个新用户
        AdminRegisterUserRequest request2 = new AdminRegisterUserRequest();
        request2.setUsername("user1");
        request2.setPassword("123456");
        request2.setRemarkName("管理员注册的用户");
        manageService.registerUserByAdmin(request2);

        // 验证用户信息在数据库的正确性
        UserDO user1 = userManager.getUser("user1");
        assertEquals("user1", user1.getUsername());
        assertEquals(EncryptionUtils.getCipher("123456"), user1.getPassword());
        assertEquals("管理员注册的用户", user1.getRemarkName());
        assertEquals(UserPositionEnum.NORMAL_USER.getValue(), user1.getIsManager());
        assertEquals(UserStatusEnum.ACTIVE_USER.getValue(), user1.getStatus());

    }

    @Test
    @Transactional
    @Rollback
    void deleteUser() {

        // 插入一个用户
        addUser("user1", "123456", "管理员");

        // 正常查到该用户
        assertNotNull(userManager.getUser("user1"));

        // 删除该用户
        DeleteUserRequest request1 = new DeleteUserRequest();
        request1.setUsername("user1");
        manageService.deleteUser(request1);

        // 无法查到该用户
        assertNull(userManager.getUser("user1"));

        // 插入一个管理员
        addAdmin("admin", "123456", "管理员");

        // 正常查到该管理员
        assertNotNull(userManager.getUser("admin"));

        // 删除该管理员
        DeleteUserRequest request2 = new DeleteUserRequest();
        request2.setUsername("admin");
        manageService.deleteUser(request2);

        // 无法查到该管理员
        assertNull(userManager.getUser("admin"));

    }

    @Test
    @Transactional
    @Rollback
    void disableUser() {
        // 插入一个用户
        addUser("user1", "123456", "用户1");

        // 启用
        ActivateUserRequest request1 = new ActivateUserRequest();
        request1.setUsername("user1");
        manageService.activateUser(request1);

        // 验证数据库: 启用
        assertEquals(UserStatusEnum.ACTIVE_USER.getValue(), userManager.getUser("user1").getStatus());

        // 禁用
        DisableUserRequest request2 = new DisableUserRequest();
        request2.setUsername("user1");
        manageService.disableUser(request2);

        // 验证数据库：禁用
        assertEquals(UserStatusEnum.BANNED_USER.getValue(), userManager.getUser("user1").getStatus());
    }

    @Test
    @Transactional
    @Rollback
    void userLogin() {

        // 插入一个用户
        addUser("user1", "123456", "用户1");

        // 启用

        ActivateUserRequest request1 = new ActivateUserRequest();
        request1.setUsername("user1");
        manageService.activateUser(request1);

        // 登录
        UserLoginRequest request2 = new UserLoginRequest();
        request2.setUsername("user1");
        request2.setPassword("123456");
        UserLoginResponse response = manageService.userLogin(request2);

        // 验证token非空
        assertNotNull(response.getToken());
        assertNotNull(redisUtils.get(RedisPreFixEnum.TOKEN.getPrefix() + "user1"));
        // 验证token一致
        assertEquals(redisUtils.get(RedisPreFixEnum.TOKEN.getPrefix() + "user1"), response.getToken());


        // 未注册用户登录
        request2.setUsername("user2");
        try {
            manageService.userLogin(request2);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof AuthenticateException);
            AuthenticateException exception = (AuthenticateException) e;
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.NO_SUCH_USER.getCode());
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.NO_SUCH_USER.getMessage());
        }

        // 输入的密码错误
        request2.setUsername("user1");
        request2.setPassword("wrong_password");
        try {
            manageService.userLogin(request2);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof AuthenticateException);
            AuthenticateException exception = (AuthenticateException) e;
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.WRONG_PASSWORD.getCode());
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.WRONG_PASSWORD.getMessage());
        }

        // 管理员在用户界面登录
        addAdmin("admin", "123456", "管理员");
        request2.setUsername("admin");
        request2.setPassword("123456");
        response = manageService.userLogin(request2);
        // 验证token非空
        assertNotNull(response.getToken());
        assertNotNull(redisUtils.get(RedisPreFixEnum.TOKEN.getPrefix() + "admin"));
        // 验证token一致
        assertEquals(redisUtils.get(RedisPreFixEnum.TOKEN.getPrefix() + "admin"), response.getToken());

        // 用户被禁用
        userManager.updateUser("user1", UserStatusEnum.BANNED_USER);
        request2.setUsername("user1");
        request2.setPassword("123456");
        try {
            manageService.userLogin(request2);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof AuthenticateException);
            AuthenticateException exception = (AuthenticateException) e;
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.BANNED_USER.getCode());
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.BANNED_USER.getMessage());
        }

        // 状态在审核用户尝试登录
        userManager.updateUser("user1", UserStatusEnum.WAITING_APPROVAL);
        request2.setUsername("user1");
        request2.setPassword("123456");
        try {
            manageService.userLogin(request2);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof AuthenticateException);
            AuthenticateException exception = (AuthenticateException) e;
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.PENDING_USER.getCode());
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.PENDING_USER.getMessage());
        }

    }

    @Test
    @Transactional
    @Rollback
    void adminLogin() {

        // 插入管理员
        addAdmin("admin", "123456", "管理员");

        // 管理员登录
        AdminLoginRequest request1 = new AdminLoginRequest();
        request1.setUsername("admin");
        request1.setPassword("123456");
        AdminLoginResponse response = manageService.adminLogin(request1);

        // 验证token非空
        assertNotNull(response.getToken());
        assertNotNull(redisUtils.get(RedisPreFixEnum.TOKEN.getPrefix() + "admin"));
        // 验证token一致
        assertEquals(redisUtils.get(RedisPreFixEnum.TOKEN.getPrefix() + "admin"), response.getToken());

        // 未注册用户登录
        request1.setUsername("admin2");
        try {
            manageService.adminLogin(request1);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof AuthenticateException);
            AuthenticateException exception = (AuthenticateException) e;
            assertEquals(exception.getErrorCode(), ResultCodeEnum.NO_SUCH_USER.getCode());
            assertEquals(exception.getErrorMsg(), ResultCodeEnum.NO_SUCH_USER.getMessage());
        }

        // 输入的密码错误
        request1.setUsername("admin");
        request1.setPassword("wrong_password");
        try {
            manageService.adminLogin(request1);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof AuthenticateException);
            AuthenticateException exception = (AuthenticateException) e;
            assertEquals(exception.getErrorCode(), ResultCodeEnum.WRONG_PASSWORD.getCode());
            assertEquals(exception.getErrorMsg(), ResultCodeEnum.WRONG_PASSWORD.getMessage());
        }

        // 用户在管理员界面登录
        addUser("user1", "123456", "用户1");
        request1.setUsername("user1");
        request1.setPassword("123456");
        try {
            manageService.adminLogin(request1);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof AuthenticateException);
            AuthenticateException exception = (AuthenticateException) e;
            assertEquals(exception.getErrorCode(), ResultCodeEnum.WRONG_USER_ROLE.getCode());
            assertEquals(exception.getErrorMsg(), ResultCodeEnum.WRONG_USER_ROLE.getMessage());
        }
    }

    @Test
    @Transactional
    @Rollback
    void userLogout() {

        // 添加用户
        addUser("user1", "123456", "用户1");

        // 启用
        ActivateUserRequest request1 = new ActivateUserRequest();
        request1.setUsername("user1");
        manageService.activateUser(request1);

        // 登录，拿到token
        UserLoginRequest request2 = new UserLoginRequest();
        request2.setUsername("user1");
        request2.setPassword("123456");
        UserLoginResponse response1 = manageService.userLogin(request2);
        String loginToken = response1.getToken();

        // 登出
        UserLogoutRequest request3 = new UserLogoutRequest();
        request3.setToken(loginToken);
        UserLogoutResponse response2 = manageService.userLogout(request3);

        // 验证响应体
        assertTrue(response2.getStatus());

        // 验证token
        assertNull(redisUtils.get(RedisPreFixEnum.TOKEN.getPrefix() + "user1"));

    }

    @Test
    @Transactional
    @Rollback
    void adminLogout() {

        // 插入管理员
        addAdmin("admin", "123456", "管理员");

        // 管理员登录
        AdminLoginRequest request1 = new AdminLoginRequest();
        request1.setUsername("admin");
        request1.setPassword("123456");
        AdminLoginResponse response1 = manageService.adminLogin(request1);
        String loginToken = response1.getToken();

        // 登出
        AdminLogoutRequest request3 = new AdminLogoutRequest();
        request3.setToken(loginToken);
        AdminLogoutResponse response2 = manageService.adminLogout(request3);

        // 验证响应体
        assertTrue(response2.getStatus());

        // 验证token
        assertNull(redisUtils.get(RedisPreFixEnum.TOKEN.getPrefix() + "admin"));
    }

    @Test
    @Transactional
    @Rollback
    void queryIfUserExist(){

        // 插入用户
        addAdmin("admin", "123456", "管理员");
        addUser("user1", "123456", "用户1");
        addUser("用户2", "123456", "用户2");

        // 构造请求
        QueryUserExistRequest request = new QueryUserExistRequest();
        QueryUserExistResponse response;

        // 查询存在的用户和管理员
        request.setUsername("user1");
        response = manageService.queryIfUserExist(request);
        assertTrue(response.getExist());

        request.setUsername("用户2");
        response = manageService.queryIfUserExist(request);
        assertTrue(response.getExist());

        request.setUsername("admin");
        response = manageService.queryIfUserExist(request);
        assertTrue(response.getExist());

        // 查询不存在的用户
        request.setUsername("user3");
        response = manageService.queryIfUserExist(request);
        assertFalse(response.getExist());


    }


    void addUser(String username, String password, String remarkName) {
        // 插入1个普通用户
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setRemarkName(remarkName);
        manageService.registerUser(request);
    }

    void addAdmin(String username, String password, String remarkName) {
        // 插入1个管理员
        AdminRegisterRequest request = new AdminRegisterRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setRemarkName(remarkName);
        manageService.registerAdmin(request);
    }


}