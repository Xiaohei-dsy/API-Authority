package com.aiit.authority.service.impl;

import javax.annotation.Resource;

import com.aiit.authority.controller.request.ListAllRoleRequest;
import com.aiit.authority.controller.response.*;
import com.aiit.authority.controller.response.vo.RoleInfoVO;
import com.aiit.authority.manager.dto.ListRoleDTO;
import com.aiit.authority.repository.entity.RoleDO;
import org.springframework.stereotype.Service;

import com.aiit.authority.controller.request.AddRoleRequest;
import com.aiit.authority.controller.request.DeleteRoleRequest;
import com.aiit.authority.controller.request.UpdateRoleRequest;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.RoleManager;
import com.aiit.authority.manager.SystemManager;
import com.aiit.authority.service.RoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleManager roleManager;

    @Resource
    private SystemManager systemManager;

    @Override
    public AddRoleResponse insertRole(AddRoleRequest addRoleRequest) {

        if (systemManager.getSystem(addRoleRequest.getSystemId()) == null) {
            throw new DatabaseException(ResultCodeEnum.NO_SUCH_SYSTEM);
        }
        AddRoleResponse addRoleResponse = new AddRoleResponse();
        Boolean result = roleManager.insertRole(addRoleRequest.getRoleName(),
                addRoleRequest.getSystemId(),
            addRoleRequest.getDescription());

        addRoleResponse.setSuccess(result);
        return addRoleResponse;
    }

    @Override
    public DeleteRoleResponse deleteRole(DeleteRoleRequest deleteRoleRequest) {
        DeleteRoleResponse deleteRoleResponse = new DeleteRoleResponse();
        Boolean result = roleManager.deleteRole(deleteRoleRequest.getRoleName(),
                deleteRoleRequest.getSystemId());
        deleteRoleResponse.setSuccess(result);
        return deleteRoleResponse;
    }

    @Override
    public UpdateRoleResponse updateRole(UpdateRoleRequest updateRoleRequest) {
        UpdateRoleResponse updateRoleResponse = new UpdateRoleResponse();
        Boolean result = roleManager.updateRole(updateRoleRequest.getRoleName(),
                updateRoleRequest.getSystemId(),
            updateRoleRequest.getDescription());
        updateRoleResponse.setSuccess(result);
        return updateRoleResponse;
    }

    @Override
    public ListRolePageResponse listRole(ListAllRoleRequest listAllRoleRequest) {
        ListRolePageResponse listRolePageResponse = new ListRolePageResponse();
        List<RoleInfoVO> roleInfoVOList = new ArrayList<>();

        ListRoleDTO listRoleDTO = roleManager.listRole(listAllRoleRequest.getCurrentPage(),
                listAllRoleRequest.getPageSize());

        PageInfo pageInfo = listRoleDTO.getPageInfo();
        List<RoleDO> roleDOList = listRoleDTO.getRoleList();

        if (roleDOList != null) {
            roleInfoVOList = roleDOList.stream().map(roleDO -> {
                RoleInfoVO roleInfoVO = new RoleInfoVO();
                roleInfoVO.setRoleName(roleDO.getRoleName());
                roleInfoVO.setSystemId(roleDO.getSystemId());
                roleInfoVO.setDescription(roleDO.getDescription());
                return roleInfoVO;
            }).collect(Collectors.toList());
        }

        listRolePageResponse.setPage(pageInfo);
        listRolePageResponse.setUserInfoList(roleInfoVOList);
        return listRolePageResponse;
    }


}
