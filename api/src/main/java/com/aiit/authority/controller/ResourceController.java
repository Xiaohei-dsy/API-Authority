package com.aiit.authority.controller;

import com.aiit.authority.controller.request.AddResourceRequest;
import com.aiit.authority.controller.request.ListResourceRequest;
import com.aiit.authority.controller.response.AddResourceResponse;
import com.aiit.authority.controller.response.ListResourceResponse;
import com.aiit.authority.service.ResourceService;
import com.aiit.authority.utils.CommonResult;
import com.aiit.authority.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/authority/resource")
@Api(tags = {"资源管理"})
@Slf4j
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @PostMapping("/add")
    @ApiOperation("添加资源,仅后端接入时使用")
    public CommonResult<AddResourceResponse> addResource(@Valid @RequestBody AddResourceRequest request) {
        log.info("请求添加资源,request:{}", JsonUtils.toJson(request));
        return CommonResult.success(resourceService.insertResource(request));
    }


    @PostMapping("/list")
    @ApiOperation("查询所有资源内容")
    public CommonResult<ListResourceResponse> listResource(@Valid @RequestBody ListResourceRequest request) {
        return CommonResult.success(resourceService.listResources(request));
    }
}
