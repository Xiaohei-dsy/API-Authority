package com.aiit.authority.manager;

import com.aiit.authority.BaseTest;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.dto.ListResourceDTO;
import com.aiit.authority.repository.entity.ResourceDO;
import com.aiit.authority.utils.RandomUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
public class ResourceManageTest extends BaseTest {

    @Resource
    private ResourceManager resourceManager;

    @Test
    @Transactional
    @Rollback
    public void deleteResourceTest() {

        // 可以删除不存在的字符串
        Assertions.assertTrue(resourceManager.deleteResource("1", "2"));

        ResourceDO resourceDO = ResourceDO.builder()
                .resourceId("1")
                .resourceType(1)
                .systemId("2")
                .resourceName("11")
                .resourceDetail(RandomUtils.randomJson())
                .description("33")
                .build();
        resourceManager.insertResource(resourceDO);
        Assertions.assertTrue(resourceManager.deleteResource("1", "2"));
    }

    @Test
    @Transactional
    @Rollback
    public void insertResourceTest() {
        ResourceDO resourceDO = ResourceDO.builder()
                .resourceId("1")
                .resourceType(1)
                .systemId("2")
                .resourceDetail("test")
                .resourceName("11")
                .description("33")
                .build();
        try {
            resourceManager.insertResource(resourceDO);
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DatabaseException);
            DatabaseException exception = (DatabaseException) e;
            Assertions.assertEquals(exception.getErrorMsg(), ResultCodeEnum.UNKNOWN_DATABASE_ERROR.getMessage());
            Assertions.assertEquals(exception.getErrorCode(), ResultCodeEnum.UNKNOWN_DATABASE_ERROR.getCode());
        }

        resourceDO.setResourceDetail(RandomUtils.randomJson());
        resourceManager.insertResource(resourceDO);

        ResourceDO resource = resourceManager.getResource("1","2");

        Assertions.assertNotNull(resource.getCreateTime());
        Assertions.assertNotNull(resource.getUpdateTime());
        Assertions.assertEquals(resourceDO.getResourceType(), resource.getResourceType());
        Assertions.assertEquals(resourceDO.getResourceId(), resource.getResourceId());
        Assertions.assertEquals(resourceDO.getDescription(), resource.getDescription());
        Assertions.assertEquals(resourceDO.getResourceName(), resource.getResourceName());
    }

    @Test
    @Transactional
    @Rollback
    public void listResourceTest() {
        for (int i = 0; i < 10; i++) {
            resourceManager.insertResource(randomBuildResource(i));
        }
        try {
            // 暂停1s 是为了验证更新时间降序逻辑
            Thread.sleep(1000L);
        } catch (Exception e) {
            log.error("暂停失败", e);
        }
        resourceManager.insertResource(randomBuildResource(10));
        ListResourceDTO resource = resourceManager.listResources(1, 5, new QueryWrapper<>());
        Assertions.assertEquals(resource.getResourceList().size(), 5);
        Assertions.assertEquals(resource.getResourceList().get(0).getResourceId(), "10");
        Assertions.assertTrue(resource.getPageInfo().getHasNext());
        Assertions.assertFalse(resource.getPageInfo().getHasPrevious());
        Assertions.assertEquals(resource.getPageInfo().getTotalPage(), 3);
        Assertions.assertEquals(resource.getPageInfo().getTotalSize(), 11);
    }


    @Test
    @Transactional
    @Rollback
    public void listResourceTest2() {
        for (int i = 0; i < 10; i++) {
            resourceManager.insertResource(randomBuildResource(i));
        }
        ListResourceDTO resource = resourceManager.listResources(2, 1, new QueryWrapper<>());
        Assertions.assertEquals(resource.getResourceList().size(), 1);
        Assertions.assertTrue(resource.getPageInfo().getHasNext());
        Assertions.assertTrue(resource.getPageInfo().getHasPrevious());
        Assertions.assertEquals(resource.getPageInfo().getTotalPage(), 10);
        Assertions.assertEquals(resource.getPageInfo().getTotalSize(), 10);
    }

    private ResourceDO randomBuildResource(Integer id) {
        return ResourceDO.builder()
                .resourceId(id.toString())
                .resourceType(RandomUtils.randomInt())
                .systemId(RandomUtils.randomString())
                .resourceDetail(RandomUtils.randomJson())
                .description(RandomUtils.randomString())
                .resourceName(RandomUtils.randomString())
                .build();
    }

}
