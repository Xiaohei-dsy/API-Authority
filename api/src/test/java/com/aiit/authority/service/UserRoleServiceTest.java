package com.aiit.authority.service;

import javax.annotation.Resource;

import com.aiit.authority.controller.response.*;
import com.aiit.authority.controller.response.vo.UserRoleVO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.aiit.authority.BaseTest;
import com.aiit.authority.controller.request.*;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.enums.UserPositionEnum;
import com.aiit.authority.enums.UserStatusEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.RoleManager;
import com.aiit.authority.manager.SystemManager;
import com.aiit.authority.manager.UserManager;
import com.aiit.authority.manager.UserRoleManager;
import com.aiit.authority.repository.entity.UserRoleDO;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserRoleServiceTest extends BaseTest {

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UserRoleManager userRoleManager;

    @Resource
    private RoleManager roleManager;

    @Resource
    private SystemManager systemManager;

    @Resource
    private UserManager userManager;

    private AddUserRoleRequest makeAddRequest() {
        AddUserRoleRequest request = new AddUserRoleRequest();
        request.setUserName("test");
        request.setRoleName("test");
        request.setSystemId("testSystem");
        return request;
    }

    private UpdateUserRoleRequest makeUpdateRequest() {
        UpdateUserRoleRequest request = new UpdateUserRoleRequest();
        request.setUserName("test");
        request.setRoleName("testRevised");
        request.setSystemId("testSystem");
        return request;
    }

    private QueryUserBySystemRequest makeQueryBySystemRequest() {
        QueryUserBySystemRequest request = new QueryUserBySystemRequest();
        request.setRoleName("test");
        request.setSystemId("testSystem");
        return request;
    }

    private DeleteUserRoleRequest makeDeleteRequest() {
        DeleteUserRoleRequest request = new DeleteUserRoleRequest();
        request.setUserName("test");
        request.setRoleName("test");
        request.setSystemId("testSystem");
        return request;
    }

    private QueryUserRoleListRequest makeQueryByUserRequest() {
        QueryUserRoleListRequest request = new QueryUserRoleListRequest();
        request.setUserName("test");
        return request;
    }

    @Test
    @Transactional
    @Rollback
    public void insertUserRoleTest() {
        AddUserRoleRequest addUserRoleRequest = makeAddRequest();

        // 验证角色不存在的异常
        userManager.insertUser("test", "123456", "测试用户", UserPositionEnum.ADMIN, UserStatusEnum.ACTIVE_USER);
        try {
            userRoleService.insertUserRole(addUserRoleRequest);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException)e;
            Assertions.assertEquals(exception.getErrorMsg(),
                    ResultCodeEnum.NO_SUCH_ROLE_SYSTEM.getMessage());
            Assertions.assertEquals(exception.getErrorCode(),
                    ResultCodeEnum.NO_SUCH_ROLE_SYSTEM.getCode());
        }

        // 验证正常插入数据的正确性
        systemManager.insertSystem("testSystem", "forTest");
        roleManager.insertRole("test", "testSystem", "test");

        AddUserRoleResponse addUserRoleResponse = userRoleService.insertUserRole(addUserRoleRequest);

        UserRoleDO userRoleDO =
            userRoleManager.getUserRole(addUserRoleRequest.getUserName(), addUserRoleRequest.getSystemId());
        Assertions.assertEquals(userRoleDO.getRoleName(), addUserRoleRequest.getRoleName());
        Assertions.assertEquals(userRoleDO.getUsername(), addUserRoleRequest.getUserName());
        Assertions.assertEquals(userRoleDO.getSystemId(), addUserRoleRequest.getSystemId());
        Assertions.assertEquals(addUserRoleResponse.getSuccess(), true);


        // 验证插入系统的另一个角色的异常情况
        roleManager.insertRole("test2", "testSystem", "test");
        AddUserRoleRequest addUserRoleRequestTwo=makeAddRequest();
        addUserRoleRequestTwo.setRoleName("test2");
        try {
            userRoleService.insertUserRole(addUserRoleRequestTwo);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException)e;
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.NO_MORE_ROLE.getMessage());
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.NO_MORE_ROLE.getCode());
        }

        //重复插入 验证无感知逻辑
        try {
            userRoleService.insertUserRole(addUserRoleRequest);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException)e;
            Assertions.assertEquals(exception.getErrorMsg(),
                    ResultCodeEnum.DUPLICATE_USER_ROLE.getMessage());
            Assertions.assertEquals(exception.getErrorCode(),
                    ResultCodeEnum.DUPLICATE_ROLE_SYSTEM.getCode());
        }

    }


    @Test
    @Transactional
    @Rollback
    public void updateUserRoleTest(){
        UpdateUserRoleRequest updateUserRoleRequest=makeUpdateRequest();
        AddUserRoleRequest addUserRoleRequest=makeAddRequest();

        //验证要修改的角色信息不存在的异常
        try {
            userRoleService.updateUserRole(updateUserRoleRequest);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException)e;
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.NO_SUCH_ROLE_SYSTEM.getMessage());
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.NO_SUCH_ROLE_SYSTEM.getCode());
        }

        //正常修改
        systemManager.insertSystem("testSystem", "forTest");
        roleManager.insertRole(addUserRoleRequest.getRoleName(), "testSystem", "forTest");
        roleManager.insertRole(updateUserRoleRequest.getRoleName(), "testSystem", "forTest");

        userRoleService.insertUserRole(addUserRoleRequest);

        UpdateUserRoleResponse updateUserRoleResponse=userRoleService.updateUserRole(updateUserRoleRequest);
        UserRoleDO userRoleDO =
                userRoleManager.getUserRole(updateUserRoleRequest.getUserName(), updateUserRoleRequest.getSystemId());
        Assertions.assertEquals(userRoleDO.getUsername(),updateUserRoleRequest.getUserName());
        Assertions.assertEquals(userRoleDO.getRoleName(),updateUserRoleRequest.getRoleName());
        Assertions.assertEquals(userRoleDO.getSystemId(),updateUserRoleRequest.getSystemId());
        Assertions.assertEquals(updateUserRoleResponse.getSuccess(),true);


        //修改已存在的数据，验证无感知逻辑
        UpdateUserRoleResponse updateUserRoleResponseTwo=userRoleService.updateUserRole(updateUserRoleRequest);
        Assertions.assertEquals(updateUserRoleResponseTwo.getSuccess(),true);

    }

    @Test
    @Transactional
    @Rollback
    public void deleteUserRoleTest(){
        DeleteUserRoleRequest deleteUserRoleRequest=makeDeleteRequest();
        AddUserRoleRequest addUserRoleRequest=makeAddRequest();
        userManager.insertUser("test", "123456", "测试用户", UserPositionEnum.ADMIN, UserStatusEnum.ACTIVE_USER);
        systemManager.insertSystem("testSystem", "forTest");
        roleManager.insertRole("test", "testSystem", "forTest");

        userRoleService.insertUserRole(addUserRoleRequest);
        DeleteUserRoleResponse deleteUserRoleResponse=userRoleService.deleteUserRole(deleteUserRoleRequest);
        UserRoleDO userRoleDO =
                userRoleManager.getUserRole(deleteUserRoleRequest.getUserName(), deleteUserRoleRequest.getSystemId());
        Assertions.assertEquals(userRoleDO,null);
        Assertions.assertEquals(deleteUserRoleResponse.getSuccess(),true);

        //重复删除 用户无感知逻辑
        DeleteUserRoleResponse deleteUserRoleResponseTwo=userRoleService.deleteUserRole(deleteUserRoleRequest);
        UserRoleDO userRoleDOTwo =
                userRoleManager.getUserRole(deleteUserRoleRequest.getUserName(), deleteUserRoleRequest.getSystemId());
        Assertions.assertEquals(userRoleDOTwo,null);
        Assertions.assertEquals(deleteUserRoleResponseTwo.getSuccess(),true);

    }

    @Test
    @Transactional
    @Rollback
    public void listRoleUserTest(){
        creatUserRoleForSystem();
        QueryUserBySystemRequest queryUserBySystemRequest=makeQueryBySystemRequest();
        queryUserBySystemRequest.setCurrentPage(1);
        queryUserBySystemRequest.setPageSize(3);
        QueryUserRoleListResponse queryUserRoleListResponse=userRoleService.listRoleUser(queryUserBySystemRequest);
        commonListTest(queryUserRoleListResponse);

        // 验证提取数据正确性，分页查询为逆时间序，第一个数据应为最后插入的数据。
        List<UserRoleVO> userRoleVOList=queryUserRoleListResponse.getUserRoleVOList();
        Assertions.assertEquals(userRoleVOList.get(0).getUserName(), "test3");
    }


    @Test
    @Transactional
    @Rollback
    public void listUserRoleTest(){
        creatUserRole();
        QueryUserRoleListRequest queryUserRoleListRequest=makeQueryByUserRequest();
        queryUserRoleListRequest.setCurrentPage(1);
        queryUserRoleListRequest.setPageSize(3);
        QueryUserRoleListResponse queryUserRoleListResponse=userRoleService.listUserRole(queryUserRoleListRequest);
        commonListTest(queryUserRoleListResponse);


        List<UserRoleVO> userRoleVOList=queryUserRoleListResponse.getUserRoleVOList();
        Assertions.assertEquals(userRoleVOList.get(0).getSystemId(), "testSystem3");
    }

    @Transactional
    @Rollback
    public void commonListTest(QueryUserRoleListResponse response){


        List<UserRoleVO> userRoleVOList=response.getUserRoleVOList();
        PageInfo pageInfo=response.getPage();

        // 当前页面为1，每页展示为2，总页数应为2，数据总条数为4。
        Assertions.assertEquals(pageInfo.getCurrentPage(), (long)1);
        Assertions.assertEquals(pageInfo.getPageSize(), (long)3);
        Assertions.assertEquals(pageInfo.getTotalSize(), (long)4);
        Assertions.assertEquals(pageInfo.getTotalPage(), (long)2);

        // 查第一页，下一页存在，上一页不存在。
        Assertions.assertTrue(pageInfo.getHasNext());
        Assertions.assertFalse(pageInfo.getHasPrevious());

        // 第一页数据条数为3
        Assertions.assertEquals(userRoleVOList.size(), 3);

    }
    @Transactional
    @Rollback
    public void creatUserRole(){
        //插入4条数据
        AddUserRoleRequest addUserRoleRequest=makeAddRequest();
        AddUserRoleRequest addUserRoleRequest1=makeAddRequest();
        AddUserRoleRequest addUserRoleRequest2=makeAddRequest();
        AddUserRoleRequest addUserRoleRequest3=makeAddRequest();

        userManager.insertUser("test", "123456", "测试用户", UserPositionEnum.ADMIN, UserStatusEnum.ACTIVE_USER);
        systemManager.insertSystem("testSystem", "forTest");
        systemManager.insertSystem("testSystem1", "forTest");
        systemManager.insertSystem("testSystem2", "forTest");
        systemManager.insertSystem("testSystem3", "forTest");

        //用户和系统绑定，所以一个用户在一个系统中只能有一个角色，用户可以有不同的系统。
        roleManager.insertRole("test", "testSystem", "forTest");
        roleManager.insertRole("test", "testSystem1", "forTest");
        roleManager.insertRole("test", "testSystem2", "forTest");
        roleManager.insertRole("test", "testSystem3", "forTest");

        addUserRoleRequest1.setSystemId("testSystem1");
        addUserRoleRequest2.setSystemId("testSystem2");
        addUserRoleRequest3.setSystemId("testSystem3");

        userRoleService.insertUserRole(addUserRoleRequest);
        userRoleService.insertUserRole(addUserRoleRequest1);
        userRoleService.insertUserRole(addUserRoleRequest2);
        // 插入几条数据可能在同一秒内完成，可能无法根据时间准确排序，手动设置延时，用于后续验证数据准确性。
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
        userRoleService.insertUserRole(addUserRoleRequest3);
    }

    @Transactional
    @Rollback
    public void creatUserRoleForSystem(){
        //插入4条数据
        AddUserRoleRequest addUserRoleRequest=makeAddRequest();
        AddUserRoleRequest addUserRoleRequest1=makeAddRequest();
        AddUserRoleRequest addUserRoleRequest2=makeAddRequest();
        AddUserRoleRequest addUserRoleRequest3=makeAddRequest();

        //创造几个不同的用户
        userManager.insertUser("test", "123456", "测试用户", UserPositionEnum.ADMIN, UserStatusEnum.ACTIVE_USER);
        userManager.insertUser("test1", "123456", "测试用户", UserPositionEnum.ADMIN, UserStatusEnum.ACTIVE_USER);
        userManager.insertUser("test2", "123456", "测试用户", UserPositionEnum.ADMIN, UserStatusEnum.ACTIVE_USER);
        userManager.insertUser("test3", "123456", "测试用户", UserPositionEnum.ADMIN, UserStatusEnum.ACTIVE_USER);


        systemManager.insertSystem("testSystem", "forTest");
        roleManager.insertRole("test", "testSystem", "forTest");

        addUserRoleRequest1.setUserName("test1");
        addUserRoleRequest2.setUserName("test2");
        addUserRoleRequest3.setUserName("test3");

        userRoleService.insertUserRole(addUserRoleRequest);
        userRoleService.insertUserRole(addUserRoleRequest1);
        userRoleService.insertUserRole(addUserRoleRequest2);
        // 插入几条数据可能在同一秒内完成，可能无法根据时间准确排序，手动设置延时，用于后续验证数据准确性。
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
        userRoleService.insertUserRole(addUserRoleRequest3);
    }

}