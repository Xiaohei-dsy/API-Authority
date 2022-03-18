package com.aiit.authority.manager.impl;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.ResourceManager;
import com.aiit.authority.manager.dto.ListResourceDTO;
import com.aiit.authority.repository.dao.ResourceDao;
import com.aiit.authority.repository.entity.ResourceDO;
import com.aiit.authority.utils.JsonUtils;
import com.aiit.authority.utils.PageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ResourceManagerImpl implements ResourceManager {

    @Resource
    private ResourceDao resourceDao;

    @Override
    public Boolean insertResource(ResourceDO resource) {
        try {
            return resourceDao.insert(resource) > 0;
        } catch (Exception e) {
            log.error("资源写入失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public ResourceDO getResource(String resourceId, String systemId) {
        QueryWrapper<ResourceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ResourceDO::getResourceId, resourceId);
        try {
            return resourceDao.selectOne(wrapper);
        } catch (Exception e) {
            log.error("查询资源失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public Boolean deleteResource(String resourceId, String systemId) {
        UpdateWrapper<ResourceDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(ResourceDO::getResourceId, resourceId)
                .eq(ResourceDO::getSystemId, systemId);
        try {
            return resourceDao.delete(updateWrapper) >= 0;
        } catch (Exception e) {
            log.error("资源删除失败", e);
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }
    }

    @Override
    public ListResourceDTO listResources(Integer currentPage, Integer pageSize, QueryWrapper<ResourceDO> queryWrapper) {
        Page<ResourceDO> page = new Page<>(currentPage, pageSize);

        IPage<Map<String, Object>> iPage;

        // 构造查询条件，如以更新时间的逆序作为标准。
        queryWrapper.lambda().orderByDesc(ResourceDO::getUpdateTime);

        try {
            iPage = resourceDao.selectMapsPage(page, queryWrapper);
        } catch (Exception e) {
            throw new DatabaseException(ResultCodeEnum.UNKNOWN_DATABASE_ERROR);
        }

        // 将查询数据转化为实体类的List,service层再构造VO类。

        List<ResourceDO> resourceList = CollectionUtils.isEmpty(iPage.getRecords()) ? new ArrayList<>()
                : JsonUtils.convert(iPage.getRecords(), new TypeReference<List<ResourceDO>>() {
        });

        PageInfo pageInfo = PageUtils.getPageInfo(page);
        return new ListResourceDTO(pageInfo, resourceList);
    }

}
