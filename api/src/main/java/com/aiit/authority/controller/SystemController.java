
package com.aiit.authority.controller;

import com.aiit.authority.controller.request.AddSystemRequest;
import com.aiit.authority.controller.request.DeleteSystemRequest;
import com.aiit.authority.controller.request.ListSystemRequest;
import com.aiit.authority.controller.request.UpdateSystemRequest;
import com.aiit.authority.controller.response.AddSystemResponse;
import com.aiit.authority.controller.response.DeleteSystemResponse;
import com.aiit.authority.controller.response.ListSystemPageResponse;
import com.aiit.authority.controller.response.UpdateSystemResponse;
import com.aiit.authority.service.SystemService;
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
@RequestMapping("/authority/system")
@Api(tags = {"系统管理"})
public class SystemController {
    @Resource
    private SystemService systemService;

    @PostMapping("/add")
    @ApiOperation("添加新系统")
    public CommonResult<AddSystemResponse> addSystem(@Valid @RequestBody AddSystemRequest request) {
        return CommonResult.success(systemService.insertSystem(request));
    }

    @PostMapping("/list")
    @ApiOperation("获取系统分页列表")
    public CommonResult<ListSystemPageResponse> listSystem(@Valid @RequestBody ListSystemRequest request) {
        return CommonResult.success(systemService.listSystem(request));
    }

    @PostMapping("/delete")
    @ApiOperation("删除系统")
    public CommonResult<DeleteSystemResponse> deleteSystem(@RequestBody DeleteSystemRequest request) {
        return CommonResult.success(systemService.deleteSystem(request));
    }

    @PostMapping("/update")
    @ApiOperation("修改系统")
    public CommonResult<UpdateSystemResponse> updateSystem(@RequestBody UpdateSystemRequest request) {
        return CommonResult.success(systemService.updateSystem(request));
    }

}
