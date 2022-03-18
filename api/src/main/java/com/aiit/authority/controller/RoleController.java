package com.aiit.authority.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.aiit.authority.controller.request.*;
import com.aiit.authority.controller.response.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aiit.authority.service.RoleService;
import com.aiit.authority.utils.CommonResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/authority/role")
@Api(tags = {"角色管理"})
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("/add")
    @ApiOperation("添加新角色")
    public CommonResult<AddRoleResponse> addRole(@Valid @RequestBody AddRoleRequest request) {
        return CommonResult.success(roleService.insertRole(request));
    }

    @PostMapping("/delete")
    @ApiOperation("删除角色")
    public CommonResult<DeleteRoleResponse> deleteRole(@RequestBody DeleteRoleRequest request) {
        return CommonResult.success(roleService.deleteRole(request));
    }

    @PostMapping("/update")
    @ApiOperation("修改角色")
    public CommonResult<UpdateRoleResponse> updateRole(@RequestBody UpdateRoleRequest request) {
        return CommonResult.success(roleService.updateRole(request));
    }

    @PostMapping("/list")
    @ApiOperation("获取角色分页列表")
    public CommonResult<ListRolePageResponse> listRole(@Valid @RequestBody ListAllRoleRequest request) {
        return CommonResult.success(roleService.listRole(request));
    }
}
