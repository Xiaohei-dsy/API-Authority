package com.aiit.authority.service.impl;

import com.aiit.authority.controller.request.AddResourceRequest;
import com.aiit.authority.controller.request.ListResourceRequest;
import com.aiit.authority.controller.response.AddResourceResponse;
import com.aiit.authority.controller.response.ListResourceResponse;
import com.aiit.authority.controller.response.vo.ResourceVO;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.ResourceManager;
import com.aiit.authority.manager.SystemManager;
import com.aiit.authority.manager.dto.ListResourceDTO;
import com.aiit.authority.repository.entity.ResourceDO;
import com.aiit.authority.repository.entity.SystemDO;
import com.aiit.authority.service.ResourceService;
import com.aiit.authority.utils.JsonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Resource
    private SystemManager systemManager;

    @Resource
    private ResourceManager resourceManager;

    @Override
    public AddResourceResponse insertResource(AddResourceRequest request) {

        // 校验系统是否存在
        SystemDO systemDO = systemManager.getSystem(request.getSystemId());
        if (Objects.isNull(systemDO)) {
            throw new DatabaseException(ResultCodeEnum.NO_SUCH_SYSTEM);
        }

        // 校验资源是否存在
        ResourceDO resourceDO = resourceManager.getResource(request.getResourceId(), request.getSystemId());
        if (Objects.nonNull(resourceDO)) {
            throw new DatabaseException(ResultCodeEnum.DUPLICATE_RESOURCE);
        }

        // 构造资源
        ResourceDO resource = new ResourceDO();
        resource.setResourceId(request.getResourceId());
        resource.setResourceType(request.getResourceType());
        resource.setSystemId(request.getSystemId());
        resource.setResourceDetail(request.getResourceDetail());
        resource.setResourceName(request.getResourceName());
        resource.setDescription(request.getDescription());

        return new AddResourceResponse(resourceManager.insertResource(resource));
    }

    @Override
    public ListResourceResponse listResources(ListResourceRequest request) {
        QueryWrapper<ResourceDO> queryWrapper = new QueryWrapper<>();
        if (Objects.nonNull(request.getSystemId())) {
            queryWrapper.lambda().eq(ResourceDO::getSystemId, request.getSystemId());
        }
        ListResourceDTO listResourceDTO = resourceManager.listResources(
                request.getCurrentPage(), request.getPageSize(), queryWrapper);
        ListResourceResponse response = new ListResourceResponse();
        response.setPage(listResourceDTO.getPageInfo());
        response.setResourceList(
                CollectionUtils.isEmpty(listResourceDTO.getResourceList()) ? new ArrayList<>()
                        : JsonUtils.convert(
                        listResourceDTO.getResourceList(), new TypeReference<List<ResourceVO>>() {
                        }));
        return response;
    }
}
