package com.aiit.authority.service;

import javax.annotation.Resource;

import com.aiit.authority.controller.request.*;
import com.aiit.authority.controller.response.*;
import com.aiit.authority.controller.response.vo.RoleInfoVO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.aiit.authority.BaseTest;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.RoleManager;
import com.aiit.authority.manager.SystemManager;
import com.aiit.authority.repository.dao.RoleDao;
import com.aiit.authority.repository.entity.RoleDO;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RoleServiceTest extends BaseTest {

    @Resource
    private RoleService roleService;

    @Resource
    private SystemManager systemManager;

    @Resource
    private RoleManager roleManager;

    @Resource
    private RoleDao roleDao;

    private AddRoleRequest makeAddRequest() {
        AddRoleRequest request = new AddRoleRequest();
        request.setRoleName("test");
        request.setSystemId("testSystem");
        request.setDescription("test");
        return request;
    }

    private DeleteRoleRequest makeDeleteRequest() {
        DeleteRoleRequest request = new DeleteRoleRequest();
        request.setRoleName("test");
        request.setSystemId("testSystem");
        return request;
    }

    private UpdateRoleRequest makeUpdateRequest() {
        UpdateRoleRequest request = new UpdateRoleRequest();
        request.setRoleName("test");
        request.setSystemId("testSystem");
        request.setDescription("修改信息");
        return request;
    }

    @Test
    @Transactional
    @Rollback
    public void insertRole() {

        AddRoleRequest addRoleRequest = makeAddRequest();

        // 验证系统不存在的异常触发
        try {
            roleService.insertRole(addRoleRequest);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException)e;
            Assertions.assertEquals(exception.getErrorMsg(),
                    ResultCodeEnum.NO_SUCH_SYSTEM.getMessage());
            Assertions.assertEquals(exception.getErrorCode(),
                    ResultCodeEnum.NO_SUCH_SYSTEM.getCode());
        }
        systemManager.insertSystem(addRoleRequest.getSystemId(), "仅test");
        AddRoleResponse addRoleResponse=roleService.insertRole(addRoleRequest);

        // 验证插入信息正确性
        RoleDO roleDO = roleManager.getRole(addRoleRequest.getRoleName(),
                addRoleRequest.getSystemId());
        Assertions.assertEquals(roleDO.getRoleName(), addRoleRequest.getRoleName());
        Assertions.assertEquals(roleDO.getSystemId(), addRoleRequest.getSystemId());

        //验证返回response
        Assertions.assertEquals(addRoleResponse.getSuccess(),true);

        // 验证重复插入的异常触发
        try {
            roleService.insertRole(addRoleRequest);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException)e;
            Assertions.assertEquals(exception.getErrorCode(),
                    ResultCodeEnum.DUPLICATE_ROLE_SYSTEM.getCode());
            Assertions.assertEquals(exception.getErrorMsg(),
                    ResultCodeEnum.DUPLICATE_ROLE_SYSTEM.getMessage());
        }

    }

    @Test
    @Transactional
    @Rollback
    public void deleteRole() {
        DeleteRoleRequest request = makeDeleteRequest();

        // 记录不存在时的异常检测
        try {
            roleService.deleteRole(request);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException)e;
            Assertions.assertEquals(exception.getErrorMsg(),
                    ResultCodeEnum.NO_SUCH_ROLE_SYSTEM.getMessage());
            Assertions.assertEquals(exception.getErrorCode(),
                    ResultCodeEnum.NO_SUCH_ROLE_SYSTEM.getCode());
        }
        // 删除成功的信息验证
        systemManager.insertSystem(request.getSystemId(), "testSystem");
        roleManager.insertRole(request.getRoleName(), request.getSystemId(), "test");

        DeleteRoleResponse deleteRoleResponse = roleService.deleteRole(request);
        Assertions.assertEquals(deleteRoleResponse.getSuccess(), true);

        // 删除完库应该为空
        Assertions.assertEquals(roleDao.selectOne(null), null);

    }

    @Test
    @Transactional
    @Rollback
    public void updateRole() {
        UpdateRoleRequest request = makeUpdateRequest();

        // 记录不存在时的异常检测
        try {
            roleService.updateRole(request);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException)e;
            Assertions.assertEquals(exception.getErrorMsg(),
                    ResultCodeEnum.NO_SUCH_ROLE_SYSTEM.getMessage());
            Assertions.assertEquals(exception.getErrorCode(),
                    ResultCodeEnum.NO_SUCH_ROLE_SYSTEM.getCode());
        }

        systemManager.insertSystem(request.getSystemId(), "testSystem");
        roleManager.insertRole(request.getRoleName(), request.getSystemId(), "test");

        UpdateRoleResponse updateRoleResponse = roleService.updateRole(request);
        Assertions.assertEquals(updateRoleResponse.getSuccess(), true);

        // 修改后的数据验证
        RoleDO roleDO = roleDao.selectOne(null);
        Assertions.assertEquals(roleDO.getDescription(), request.getDescription());
        Assertions.assertEquals(roleDO.getRoleName(), request.getRoleName());
        Assertions.assertEquals(roleDO.getSystemId(), request.getSystemId());

    }

    @Test
    @Transactional
    @Rollback
    public void listRoleTest() {
        //插入角色数据
        systemManager.insertSystem("testSystem", "testSystem");
        roleManager.insertRole("test", "testSystem", "test");
        roleManager.insertRole("test1", "testSystem", "test");
        roleManager.insertRole("test2", "testSystem", "test");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
        roleManager.insertRole("test3", "testSystem", "test");

        ListAllRoleRequest listAllRoleRequest=new ListAllRoleRequest(){{
            setCurrentPage(1);
            setPageSize(3);
        }};

        ListRolePageResponse response=roleService.listRole(listAllRoleRequest);
        List<RoleInfoVO> roleInfoVOList=response.getUserInfoList();
        PageInfo pageInfo=response.getPage();

        // 当前页面为1，每页展示为3，总页数应为2，数据总条数为4。
        Assertions.assertEquals(pageInfo.getCurrentPage(), (long)1);
        Assertions.assertEquals(pageInfo.getPageSize(), (long)3);
        Assertions.assertEquals(pageInfo.getTotalSize(), (long)4);
        Assertions.assertEquals(pageInfo.getTotalPage(), (long)2);

        // 查第一页，下一页存在，上一页不存在。
        Assertions.assertTrue(pageInfo.getHasNext());
        Assertions.assertFalse(pageInfo.getHasPrevious());

        // 第一页数据条数为3
        Assertions.assertEquals(roleInfoVOList.size(), 3);

        // 验证提取数据正确性，分页查询为逆时间序，第一个数据应为最后插入的数据。
        Assertions.assertEquals(roleInfoVOList.get(0).getRoleName(), "test3");
    }

}