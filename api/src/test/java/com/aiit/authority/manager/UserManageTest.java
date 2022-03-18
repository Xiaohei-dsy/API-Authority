package com.aiit.authority.manager;

import com.aiit.authority.BaseTest;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.enums.UserPositionEnum;
import com.aiit.authority.enums.UserStatusEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.repository.entity.UserDO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

public class UserManageTest extends BaseTest {

    @Resource
    private UserManager userManager;


    @Test
    @Transactional
    @Rollback
    public void insertUserTest() {

        // 插入管理员
        userManager.insertUser("admin", "202cb962ac59075b964b07152d234b70", "默认用户",
                UserPositionEnum.ADMIN, UserStatusEnum.WAITING_APPROVAL);

        // 验证数据库长度
        List<UserDO> userDOList = userManager.listUsers(null);
        Assertions.assertEquals(1, userDOList.size());

        // 验证插入对象值的正确性
        UserDO admin = userManager.getUser("admin");
        Assertions.assertEquals("admin", admin.getUsername());
        Assertions.assertEquals("默认用户", admin.getRemarkName());
        Assertions.assertEquals("202cb962ac59075b964b07152d234b70", admin.getPassword());
        Assertions.assertEquals(UserPositionEnum.ADMIN.getValue(), admin.getIsManager());
        Assertions.assertEquals(UserStatusEnum.WAITING_APPROVAL.getValue(), admin.getStatus());

        // 插入普通用户
        userManager.insertUser("user1", "202cb962ac59075b964b07152d234b70", "用户1",
                UserPositionEnum.NORMAL_USER, UserStatusEnum.WAITING_APPROVAL);

        // 验证数据库长度
        userDOList = userManager.listUsers(null);
        Assertions.assertEquals(2, userDOList.size());

        // 验证插入对象值的正确性
        UserDO user = userManager.getUser("user1");
        Assertions.assertEquals("user1", user.getUsername());
        Assertions.assertEquals("用户1", user.getRemarkName());
        Assertions.assertEquals("202cb962ac59075b964b07152d234b70", user.getPassword());
        Assertions.assertEquals(UserPositionEnum.NORMAL_USER.getValue(), user.getIsManager());
        Assertions.assertEquals(UserStatusEnum.WAITING_APPROVAL.getValue(), user.getStatus());

        // 插入重复用户
        try {
            userManager.insertUser("user1", "202cb962ac59075b964b07152d234b70", "默认用户",
                    UserPositionEnum.ADMIN, UserStatusEnum.WAITING_APPROVAL);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException) e;
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.DUPLICATE_USER.getCode());
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.DUPLICATE_USER.getMessage());
        }
    }

    @Test
    @Transactional
    @Rollback
    public void listUsersTest() {
        // 插入3个用户
        userManager.insertUser("user1", "202cb962ac59075b964b07152d234b70", "用户1",
                UserPositionEnum.NORMAL_USER, UserStatusEnum.WAITING_APPROVAL);
        userManager.insertUser("user2", "202cb962ac59075b964b07152d234b70", "用户2",
                UserPositionEnum.NORMAL_USER, UserStatusEnum.WAITING_APPROVAL);
        userManager.insertUser("admin", "202cb962ac59075b964b07152d234b70", "管理员",
                UserPositionEnum.ADMIN, UserStatusEnum.WAITING_APPROVAL);
        userManager.listUsers(null);
        List<UserDO> userDOList = userManager.listUsers(null);
        Assertions.assertEquals(3, userDOList.size());
    }

    @Test
    @Transactional
    @Rollback
    public void updateUserTest() {

        // 插入2个新用户并尝试更新
        userManager.insertUser("user1", "202cb962ac59075b964b07152d234b70", "用户1",
                UserPositionEnum.NORMAL_USER, UserStatusEnum.WAITING_APPROVAL);
        userManager.insertUser("admin", "202cb962ac59075b964b07152d234b70", "管理员",
                UserPositionEnum.ADMIN, UserStatusEnum.WAITING_APPROVAL);
        userManager.updateUser("user1", UserStatusEnum.BANNED_USER);
        userManager.updateUser("admin", UserStatusEnum.ACTIVE_USER);

        // 验证更新值
        UserDO user1 = userManager.getUser("user1");
        Assertions.assertEquals(UserStatusEnum.BANNED_USER.getValue(), user1.getStatus());
        UserDO admin = userManager.getUser("admin");
        Assertions.assertEquals(UserStatusEnum.ACTIVE_USER.getValue(), admin.getStatus());

        // 更新不存在的用户
        try {
            userManager.updateUser("user2", UserStatusEnum.BANNED_USER);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException) e;
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.NO_SUCH_USER.getCode());
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.NO_SUCH_USER.getMessage());
        }
    }

    @Test
    @Transactional
    @Rollback
    public void deleteUserTest() {

        // 插入2个新用户并删除其中的一个
        userManager.insertUser("user1", "202cb962ac59075b964b07152d234b70", "用户1",
                UserPositionEnum.NORMAL_USER, UserStatusEnum.WAITING_APPROVAL);
        userManager.insertUser("admin", "202cb962ac59075b964b07152d234b70", "管理员",
                UserPositionEnum.ADMIN, UserStatusEnum.WAITING_APPROVAL);
        userManager.deleteUser("user1");

        // 验证数据库表长度
        List<UserDO> userDOList = userManager.listUsers(null);
        Assertions.assertEquals(1, userDOList.size());

        // 验证删除成功
        UserDO user1 = userManager.getUser("user1");
        Assertions.assertNull(user1);


        // 删除不存在的用户
        try {
            userManager.deleteUser("user1");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException) e;
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.NO_SUCH_USER.getCode());
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.NO_SUCH_USER.getMessage());
        }
    }


    @Test
    @Transactional
    @Rollback
    public void getUserTest() {

        // 插入2个新用户
        userManager.insertUser("user1", "202cb962ac59075b964b07152d234b70", "用户1",
                UserPositionEnum.NORMAL_USER, UserStatusEnum.WAITING_APPROVAL);
        userManager.insertUser("admin", "202cb962ac59075b964b07152d234b70", "管理员",
                UserPositionEnum.ADMIN, UserStatusEnum.WAITING_APPROVAL);

        // 验证获取用户属性正确
        UserDO user1 = userManager.getUser("user1");
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("user1", user1.getUsername());
        Assertions.assertEquals("用户1", user1.getRemarkName());
        Assertions.assertEquals("202cb962ac59075b964b07152d234b70", user1.getPassword());
        Assertions.assertEquals(UserPositionEnum.NORMAL_USER.getValue(), user1.getIsManager());
        Assertions.assertEquals(UserStatusEnum.WAITING_APPROVAL.getValue(), user1.getStatus());

        // 验证获取用户属性正确
        UserDO admin = userManager.getUser("admin");
        Assertions.assertNotNull(admin);
        Assertions.assertEquals("admin", admin.getUsername());
        Assertions.assertEquals("管理员", admin.getRemarkName());
        Assertions.assertEquals("202cb962ac59075b964b07152d234b70", admin.getPassword());
        Assertions.assertEquals(UserPositionEnum.ADMIN.getValue(), admin.getIsManager());
        Assertions.assertEquals(UserStatusEnum.WAITING_APPROVAL.getValue(), admin.getStatus());

        // 获取不存在的用户
        Assertions.assertNull(userManager.getUser("user2"));

    }


}
