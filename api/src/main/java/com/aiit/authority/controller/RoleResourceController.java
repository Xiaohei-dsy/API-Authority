package com.aiit.authority.controller;

import com.aiit.authority.controller.request.AddRoleResourceRequest;
import com.aiit.authority.controller.request.DeleteRoleResourceRequest;
import com.aiit.authority.controller.request.QueryRoleResourceListRequest;
import com.aiit.authority.controller.response.AddRoleResourceResponse;
import com.aiit.authority.controller.response.DeleteRoleResourceResponse;
import com.aiit.authority.controller.response.QueryRoleResourceListResponse;
import com.aiit.authority.service.RoleResourceService;
import com.aiit.authority.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/authority/roleResource")
@Api(tags = {"角色资源管理"})
public class RoleResourceController {

    @Resource
    private RoleResourceService roleResourceService;

    @PostMapping("/admin/add")
    @ApiOperation("管理员给角色添加资源")
    public CommonResult<AddRoleResourceResponse> addRoleResource(@RequestBody AddRoleResourceRequest request) {
        return CommonResult.success(roleResourceService.insertRoleResource(request));
    }

    @PostMapping("/list")
    @ApiOperation("根据角色名查询资源信息")
    public CommonResult<QueryRoleResourceListResponse> listRoleResourceList(
            @Valid @RequestBody QueryRoleResourceListRequest request) {
        return CommonResult.success(roleResourceService.listRoleResource(request));
    }

    @PostMapping("/admin/delete")
    @ApiOperation("管理员删除角色的资源信息")
    public CommonResult<DeleteRoleResourceResponse> deleteRoleResource(@RequestBody DeleteRoleResourceRequest request) {
        return CommonResult.success(roleResourceService.deleteRoleResource(request));
    }

}
