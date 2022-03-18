package com.aiit.authority.manager;

import com.aiit.authority.BaseTest;
import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.dto.ListRoleResourceDTO;
import com.aiit.authority.repository.entity.ResourceDO;
import com.aiit.authority.repository.entity.RoleResourceDO;
import com.aiit.authority.utils.RandomUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RoleResourceManageTest extends BaseTest {

    @Resource
    private RoleResourceManager roleResourceManager;

    @Resource
    private ResourceManager resourceManager;

    @Test
    @Transactional
    @Rollback
    public void insertRoleResourceTest() {
        //验证资源不存在的异常
        try {
            roleResourceManager.insertRoleResource("test", "testSystem", "test");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException) e;
            Assertions.assertEquals(exception.getErrorMsg(),
                    ResultCodeEnum.NO_SUCH_RESOURCE.getMessage());
            Assertions.assertEquals(exception.getErrorCode(),
                    ResultCodeEnum.NO_SUCH_RESOURCE.getCode());
        }
        ResourceDO resourceDO = new ResourceDO() {{
            setResourceId("test");
            setSystemId("testSystem");
            setResourceName(RandomUtils.randomString());
            setDescription(RandomUtils.randomString());
        }};
        resourceManager.insertResource(resourceDO);
        Boolean result = roleResourceManager.insertRoleResource("test", resourceDO.getSystemId(),
                resourceDO.getResourceId());
        RoleResourceDO roleResourceDO = roleResourceManager.getRoleResource("test", resourceDO.getSystemId(),
                resourceDO.getResourceId());
        Assertions.assertTrue(result);
        Assertions.assertEquals("test", roleResourceDO.getRoleName());
        Assertions.assertEquals(resourceDO.getResourceId(), roleResourceDO.getResourceId());
        Assertions.assertEquals(resourceDO.getSystemId(), roleResourceDO.getSystemId());

        //重复插入的异常
        try {
            roleResourceManager.insertRoleResource("test", resourceDO.getSystemId(),
                    resourceDO.getResourceId());
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException) e;
            Assertions.assertEquals(exception.getErrorMsg(),
                    ResultCodeEnum.DUPLICATE_ROLE_RESOURCE.getMessage());
            Assertions.assertEquals(exception.getErrorCode(),
                    ResultCodeEnum.DUPLICATE_ROLE_RESOURCE.getCode());
        }
    }

    @Test
    @Transactional
    @Rollback
    public void insertRoleResourceTest2() {
        ResourceDO resourceDO = new ResourceDO() {{
            setResourceId("test1");
            setSystemId("testSystem");
            setResourceName(RandomUtils.randomString());
            setDescription(RandomUtils.randomString());
        }};
        resourceManager.insertResource(resourceDO);
        roleResourceManager.insertRoleResource("test", resourceDO.getSystemId(),
                resourceDO.getResourceId());

        ResourceDO resourceDO2 = new ResourceDO() {{
            setResourceId("test2");
            setSystemId("testSystem");
            setResourceName(RandomUtils.randomString());
            setDescription(RandomUtils.randomString());
        }};
        resourceManager.insertResource(resourceDO2);
        roleResourceManager.insertRoleResource("test", resourceDO2.getSystemId(),
                resourceDO2.getResourceId());

        ResourceDO resourceDO3 = new ResourceDO() {{
            setResourceId("test3");
            setSystemId("testSystem");
            setResourceName(RandomUtils.randomString());
            setDescription(RandomUtils.randomString());
        }};
        resourceManager.insertResource(resourceDO3);
        roleResourceManager.insertRoleResource("test", resourceDO3.getSystemId(),
                resourceDO3.getResourceId());

        ResourceDO resourceDO4 = new ResourceDO() {{
            setResourceId("test4");
            setSystemId("testSystem");
            setResourceName(RandomUtils.randomString());
            setDescription(RandomUtils.randomString());
        }};
        resourceManager.insertResource(resourceDO4);
        roleResourceManager.insertRoleResource("test", resourceDO4.getSystemId(),
                resourceDO4.getResourceId());

        ResourceDO resourceDO5 = new ResourceDO() {{
            setResourceId("test5");
            setSystemId("testSystem");
            setResourceName(RandomUtils.randomString());
            setDescription(RandomUtils.randomString());
        }};
        resourceManager.insertResource(resourceDO5);

        try {
            roleResourceManager.insertRoleResource("test", resourceDO5.getSystemId(),
                    resourceDO5.getResourceId());
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException) e;
            Assertions.assertEquals(exception.getErrorMsg(),
                    ResultCodeEnum.OVER_NUM_RESOURCE.getMessage());
            Assertions.assertEquals(exception.getErrorCode(),
                    ResultCodeEnum.OVER_NUM_RESOURCE.getCode());
        }

    }




    @Test
    @Transactional
    @Rollback
    public void listRoleResourceTest() {
        //插入数据
        insertRoleResource("test1");
        insertRoleResource("test2");
        insertRoleResource("test3");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
        insertRoleResource("test4");

        ListRoleResourceDTO listRoleResourceDTO = roleResourceManager.listRoleResource("test", "testSystem",1, 3);
        List<RoleResourceDO> roleResourceDOList = listRoleResourceDTO.getList();
        PageInfo pageInfo = listRoleResourceDTO.getPageInfo();

        // 当前页面为1，每页展示为2，总页数应为2，数据总条数为4。
        Assertions.assertEquals(pageInfo.getCurrentPage(), (long) 1);
        Assertions.assertEquals(pageInfo.getPageSize(), (long) 3);
        Assertions.assertEquals(pageInfo.getTotalSize(), (long) 4);
        Assertions.assertEquals(pageInfo.getTotalPage(), (long) 2);

        // 查第一页，下一页存在，上一页不存在。
        Assertions.assertTrue(pageInfo.getHasNext());
        Assertions.assertFalse(pageInfo.getHasPrevious());

        // 第一页数据条数为3
        Assertions.assertEquals(roleResourceDOList.size(), 3);

        // 验证提取数据正确性，分页查询为逆时间序，第一个数据应为最后插入的数据。
        Assertions.assertEquals(roleResourceDOList.get(0).getResourceId(), "test4");

    }


    @Test
    @Transactional
    @Rollback
    public void deleteRoleResourceTest() {
        insertRoleResource("test");
        Boolean result = roleResourceManager.deleteRoleResource("test", "testSystem", "test");
        RoleResourceDO roleResourceDO = roleResourceManager.getRoleResource
                ("test", "testSystem", "test");
        Assertions.assertNull(roleResourceDO);
        Assertions.assertTrue(result);
    }

    @Test
    @Transactional
    @Rollback
    public void getRoleResourceTest() {
        insertRoleResource("test");
        RoleResourceDO roleResourceDO = roleResourceManager.getRoleResource("test", "testSystem", "test");
        Assertions.assertEquals(roleResourceDO.getRoleName(), "test");
        Assertions.assertEquals(roleResourceDO.getSystemId(), "testSystem");
        Assertions.assertEquals(roleResourceDO.getResourceId(), "test");
    }

    public void insertRoleResource(String resourceId) {
        ResourceDO resourceDO = new ResourceDO() {{
            setResourceId(resourceId);
            setSystemId("testSystem");
            setResourceName(RandomUtils.randomString());
            setDescription(RandomUtils.randomString());
        }};
        resourceManager.insertResource(resourceDO);
        roleResourceManager.insertRoleResource("test", resourceDO.getSystemId(),
                resourceDO.getResourceId());
    }

}
