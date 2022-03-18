package com.aiit.authority.service.impl;

import com.aiit.authority.controller.request.AddRoleResourceRequest;
import com.aiit.authority.controller.request.DeleteRoleResourceRequest;
import com.aiit.authority.controller.request.QueryRoleResourceListRequest;
import com.aiit.authority.controller.response.AddRoleResourceResponse;
import com.aiit.authority.controller.response.DeleteRoleResourceResponse;
import com.aiit.authority.controller.response.QueryRoleResourceListResponse;
import com.aiit.authority.controller.response.vo.RoleResourceVO;
import com.aiit.authority.manager.RoleResourceManager;
import com.aiit.authority.manager.dto.ListRoleResourceDTO;
import com.aiit.authority.repository.entity.RoleResourceDO;
import com.aiit.authority.service.RoleResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class RoleResourceServiceImpl implements RoleResourceService {

    @Resource
    private RoleResourceManager roleResourceManager;

    @Override
    public AddRoleResourceResponse insertRoleResource(AddRoleResourceRequest request) {
        AddRoleResourceResponse response = new AddRoleResourceResponse();
        Boolean result = roleResourceManager.insertRoleResource(request.getRoleName(),
                request.getSystemId(),
            request.getResourceId());
        response.setSuccess(result);
        return response;
    }

    @Override
    public DeleteRoleResourceResponse deleteRoleResource(DeleteRoleResourceRequest request) {
        DeleteRoleResourceResponse response = new DeleteRoleResourceResponse();
        Boolean result = roleResourceManager.deleteRoleResource(request.getRoleName(),
                request.getSystemId(),
            request.getResourceId());
        response.setSuccess(result);
        return response;
    }

    @Override
    public QueryRoleResourceListResponse listRoleResource(QueryRoleResourceListRequest request) {
        QueryRoleResourceListResponse response = new QueryRoleResourceListResponse();
        List<RoleResourceVO> roleResourceVOList = new ArrayList<>();
        ListRoleResourceDTO listRoleResourceDTO = roleResourceManager.listRoleResource(request.getRoleName(),
            request.getSystemId(), request.getCurrentPage(), request.getPageSize());
        List<RoleResourceDO> roleResourceDOList = listRoleResourceDTO.getList();

        if (roleResourceDOList != null) {
            roleResourceVOList = roleResourceDOList.stream().map(roleResourceDO -> {
                RoleResourceVO roleResourceVO = new RoleResourceVO();
                roleResourceVO.setRoleName(roleResourceDO.getRoleName());
                roleResourceVO.setSystemId(roleResourceDO.getSystemId());
                roleResourceVO.setResourceId(roleResourceDO.getResourceId());
                return roleResourceVO;
            }).collect(Collectors.toList());
        }
        response.setPage(listRoleResourceDTO.getPageInfo());
        response.setRoleResourceVOList(roleResourceVOList);
        return response;
    }
}
