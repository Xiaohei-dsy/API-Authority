package com.aiit.authority.service;

import javax.annotation.Resource;

import com.aiit.authority.controller.response.QueryRoleResourceListResponse;
import com.aiit.authority.manager.ResourceManager;
import com.aiit.authority.manager.RoleResourceManager;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.aiit.authority.BaseTest;
import com.aiit.authority.controller.request.QueryRoleResourceListRequest;
import com.aiit.authority.repository.entity.ResourceDO;
import com.aiit.authority.utils.RandomUtils;

import java.util.concurrent.TimeUnit;

public class RoleResourceServiceTest extends BaseTest {

    @Resource
    private RoleResourceService roleResourceService;

    @Resource
    private ResourceManager resourceManager;

    @Resource
    private RoleResourceManager roleResourceManager;

    @Test
    @Transactional
    @Rollback
    public void listRoleResourceTest() {
        //添加数据
        creatListMessage("test1");
        creatListMessage("test2");
        roleResourceManager.insertRoleResource("test","testSystem","test1");
        roleResourceManager.insertRoleResource("test","testSystem2","test1");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
        roleResourceManager.insertRoleResource("test","testSystem3","test1");
        roleResourceManager.insertRoleResource("test2","testSystem2","test1");
        roleResourceManager.insertRoleResource("test3","testSystem","test1");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
        roleResourceManager.insertRoleResource("test3","testSystem2","test2");
        // 验证输入参数缺少时的查询情况

        // 1. 缺少系统名，仅输入角色名与分页信息。
        QueryRoleResourceListRequest request=creatListRequest("test",null,1,3);
        QueryRoleResourceListResponse response=roleResourceService.listRoleResource(request);
        Assertions.assertEquals(response.getRoleResourceVOList().size(),3);
        Assertions.assertEquals(response.getRoleResourceVOList().get(0).getRoleName(),"test");
        Assertions.assertEquals(response.getRoleResourceVOList().get(0).getSystemId(),"testSystem3");

        // 2. 缺少角色名，仅输入系统名与分页信息。
        QueryRoleResourceListRequest request2=creatListRequest(null,"testSystem",1,3);
        QueryRoleResourceListResponse response2=roleResourceService.listRoleResource(request2);
        Assertions.assertEquals(response2.getRoleResourceVOList().size(),2);
        Assertions.assertEquals(response2.getRoleResourceVOList().get(0).getSystemId(),"testSystem");
        Assertions.assertEquals(response2.getRoleResourceVOList().get(0).getRoleName(),"test3");


        // 3. 缺少系统&角色名，仅输入分页信息。
        QueryRoleResourceListRequest request3=creatListRequest(null,null,1,6);
        QueryRoleResourceListResponse response3=roleResourceService.listRoleResource(request3);
        Assertions.assertEquals(response3.getRoleResourceVOList().size(),6);
        Assertions.assertEquals(response3.getRoleResourceVOList().get(0).getResourceId(),"test2");


    }

    public QueryRoleResourceListRequest creatListRequest(String roleName, String systemId, Integer currentPage,
        Integer pageSize) {
        QueryRoleResourceListRequest request = new QueryRoleResourceListRequest();
        if (roleName != null) {
            request.setRoleName(roleName);
        } else if (systemId != null) {
            request.setSystemId(systemId);
        }
        request.setCurrentPage(currentPage);
        request.setPageSize(pageSize);
        return request;
    }

    public void creatListMessage(String resourceId) {
        ResourceDO resourceDO = new ResourceDO() {
            {
                setResourceId(resourceId);
                setSystemId("testSystem");
                setResourceName(RandomUtils.randomString());
                setDescription(RandomUtils.randomString());
            }
        };
        resourceManager.insertResource(resourceDO);
    }

}
