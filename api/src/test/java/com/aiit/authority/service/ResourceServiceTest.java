package com.aiit.authority.service;

import com.aiit.authority.BaseTest;
import com.aiit.authority.controller.request.AddResourceRequest;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.ResourceManager;
import com.aiit.authority.manager.SystemManager;
import com.aiit.authority.repository.entity.ResourceDO;
import com.aiit.authority.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class ResourceServiceTest extends BaseTest {

    @Resource
    private ResourceService resourceService;

    @Resource
    private SystemManager systemManager;

    @Resource
    private ResourceManager resourceManager;

    @Test
    @Transactional
    @Rollback
    public void insertResourceTest() {

        AddResourceRequest request = makeRequest();

        // 系统不存在
        try {
            resourceService.insertResource(request);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException) e;
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.NO_SUCH_SYSTEM.getCode());
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.NO_SUCH_SYSTEM.getMessage());
        }

        systemManager.insertSystem(request.getSystemId(), "测试系统");
        resourceService.insertResource(request);

        // 插入成功
        ResourceDO resourceDO = resourceManager.getResource(request.getResourceId(), request.getSystemId());
        Assertions.assertEquals(resourceDO.getResourceId(), request.getResourceId());
        Assertions.assertEquals(resourceDO.getSystemId(), request.getSystemId());
        Assertions.assertEquals(
                JsonUtils.fromJson(resourceDO.getResourceDetail(), Detail.class),
                JsonUtils.fromJson(request.getResourceDetail(), Detail.class));
        Assertions.assertEquals(resourceDO.getResourceType(), request.getResourceType());

        // 重复插入
        try {
            resourceService.insertResource(request);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException) e;
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.DUPLICATE_RESOURCE.getCode());
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.DUPLICATE_RESOURCE.getMessage());
        }
    }


    private AddResourceRequest makeRequest() {
        AddResourceRequest request = new AddResourceRequest();
        request.setResourceId("test");
        request.setResourceName("name");
        request.setDescription("描述");
        request.setResourceDetail(JsonUtils.toJson(new Detail(0, "测试资源")));
        request.setSystemId("test");
        request.setResourceType(0);
        return request;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Detail {

        private Integer type;

        private String value;
    }

}
