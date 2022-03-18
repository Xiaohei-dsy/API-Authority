package com.aiit.authority.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aiit.authority.controller.request.*;
import com.aiit.authority.controller.response.*;
import com.aiit.authority.controller.response.vo.UserRoleVO;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.manager.RoleManager;
import com.aiit.authority.manager.UserRoleManager;
import com.aiit.authority.manager.dto.ListUserRoleDTO;
import com.aiit.authority.repository.entity.UserRoleDO;
import com.aiit.authority.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private RoleManager roleManager;

    @Resource
    private UserRoleManager userRoleManager;

    @Override
    public AddUserRoleResponse insertUserRole(AddUserRoleRequest request) {
        if (roleManager.getRole(request.getRoleName(), request.getSystemId()) == null) {
            throw new DatabaseException(ResultCodeEnum.NO_SUCH_ROLE_SYSTEM);
        }
        AddUserRoleResponse addUserRoleResponse = new AddUserRoleResponse();
        Boolean result =
            userRoleManager.insertRole(request.getUserName(), request.getRoleName(),
                    request.getSystemId());
        addUserRoleResponse.setSuccess(result);
        return addUserRoleResponse;
    }

    @Override
    public DeleteUserRoleResponse deleteUserRole(DeleteUserRoleRequest request) {
        DeleteUserRoleResponse deleteUserRoleResponse = new DeleteUserRoleResponse();
        Boolean result =
            userRoleManager.deleteUserRole(request.getUserName(), request.getRoleName(),
                    request.getSystemId());
        deleteUserRoleResponse.setSuccess(result);
        return deleteUserRoleResponse;
    }

    // 根据用户名查询角色列表
    @Override
    public QueryUserRoleListResponse listUserRole(QueryUserRoleListRequest request) {
        ListUserRoleDTO userRoleDOList =
            userRoleManager.listUserRoleByUser(request.getUserName(),
                    request.getCurrentPage(), request.getPageSize());
        return convertToResponse(userRoleDOList);
    }

    // 根据角色信息查询用户列表
    @Override
    public QueryUserRoleListResponse listRoleUser(QueryUserBySystemRequest request) {
        ListUserRoleDTO userRoleDOList = userRoleManager.listUserRoleBySystem(
                request.getRoleName(),
            request.getSystemId(), request.getCurrentPage(), request.getPageSize());
        return convertToResponse(userRoleDOList);
    }

    public QueryUserRoleListResponse convertToResponse(ListUserRoleDTO listUserRoleDTO) {
        QueryUserRoleListResponse queryUserRoleListResponse = new QueryUserRoleListResponse();
        List<UserRoleVO> userRoleVOList = new ArrayList<>();
        List<UserRoleDO> userRoleDOList = listUserRoleDTO.getList();
        PageInfo pageInfo = listUserRoleDTO.getPageInfo();
        if (userRoleDOList != null) {
            userRoleVOList = userRoleDOList.stream().map(userRoleDOInfo -> {
                UserRoleVO userRoleVO = new UserRoleVO();
                userRoleVO.setUserName(userRoleDOInfo.getUsername());
                userRoleVO.setRoleName(userRoleDOInfo.getRoleName());
                userRoleVO.setSystemId(userRoleDOInfo.getSystemId());
                return userRoleVO;
            }).collect(Collectors.toList());
        }
        queryUserRoleListResponse.setUserRoleVOList(userRoleVOList);
        queryUserRoleListResponse.setPage(pageInfo);
        return queryUserRoleListResponse;
    }

    @Override
    public UpdateUserRoleResponse updateUserRole(UpdateUserRoleRequest request) {
        UpdateUserRoleResponse updateUserRoleResponse = new UpdateUserRoleResponse();
        // 若已存在修改后的用户系统角色组合，直接返回true，用户不感知已存在的逻辑，只知道存在修改后的数据。
        if (userRoleManager.isValidUserRole(request.getUserName(), request.getRoleName(),
                request.getSystemId())) {
            updateUserRoleResponse.setSuccess(true);
        } else if (roleManager.getRole(request.getRoleName(), request.getSystemId()) == null) {
            throw new DatabaseException(ResultCodeEnum.NO_SUCH_ROLE_SYSTEM);
        }
        Boolean result =
            userRoleManager.updateUserRole(request.getUserName(), request.getRoleName(),
                    request.getSystemId());
        updateUserRoleResponse.setSuccess(result);
        return updateUserRoleResponse;
    }

}
