package com.aiit.authority.manager;

import com.aiit.authority.manager.dto.ListResourceDTO;
import com.aiit.authority.repository.entity.ResourceDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public interface ResourceManager {

    Boolean insertResource(ResourceDO resource);

    ResourceDO getResource(String resourceId, String systemId);

    Boolean deleteResource(String resourceId, String systemId);

    ListResourceDTO listResources(Integer currentPage, Integer pageSize, QueryWrapper<ResourceDO> queryWrapper);

}
