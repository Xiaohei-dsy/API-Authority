package com.aiit.authority.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.aiit.authority.controller.request.*;
import com.aiit.authority.controller.response.*;
import com.aiit.authority.service.UserRoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aiit.authority.utils.CommonResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/authority/userRole")
@Api(tags = {"用户角色管理"})
public class UserRoleController {

    @Resource
    private UserRoleService userRoleService;

    @PostMapping("/admin/add")
    @ApiOperation("管理员添加角色")
    public CommonResult<AddUserRoleResponse> addUserRole(@RequestBody AddUserRoleRequest request) {
        return CommonResult.success(userRoleService.insertUserRole(request));
    }

    @PostMapping("/list")
    @ApiOperation("根据用户名查询角色信息")
    public CommonResult<QueryUserRoleListResponse> listUserRoleList(
            @Valid @RequestBody QueryUserRoleListRequest request) {
        return CommonResult.success(userRoleService.listUserRole(request));
    }

    @PostMapping("/listByRole")
    @ApiOperation("根据角色信息查询用户列表")
    public CommonResult<QueryUserRoleListResponse> listUserRoleBySystemList(
            @Valid @RequestBody QueryUserBySystemRequest request) {
        return CommonResult.success(userRoleService.listRoleUser(request));
    }

    @PostMapping("/admin/delete")
    @ApiOperation("管理员删除用户的角色信息")
    public CommonResult<DeleteUserRoleResponse> deleteUserRole(@RequestBody DeleteUserRoleRequest request) {
        return CommonResult.success(userRoleService.deleteUserRole(request));
    }

    @PostMapping("/admin/update")
    @ApiOperation("管理员修改用户的角色信息")
    public CommonResult<UpdateUserRoleResponse> updateUserRole(@RequestBody UpdateUserRoleRequest request) {
        return CommonResult.success(userRoleService.updateUserRole(request));
    }
}
