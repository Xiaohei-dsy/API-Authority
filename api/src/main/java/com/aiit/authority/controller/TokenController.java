package com.aiit.authority.controller;

import com.aiit.authority.controller.request.DecodeTokenRequest;
import com.aiit.authority.controller.request.ValidateTokenRequest;
import com.aiit.authority.controller.response.DecodeTokenResponse;
import com.aiit.authority.controller.response.ValidateTokenResponse;
import com.aiit.authority.service.TokenService;
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
@RequestMapping("/authority/token/")
@Api(tags = {"管理员相关操作"})
public class TokenController {

    @Resource
    private TokenService tokenService;

    @PostMapping("/validate")
    @ApiOperation("查询token是否合法")
    public CommonResult<ValidateTokenResponse> validateToken(@Valid @RequestBody ValidateTokenRequest request) {
        ValidateTokenResponse response = new ValidateTokenResponse();
        response.setIsValid(tokenService.validateToken(request.getToken()).getValid());
        return CommonResult.success(response);
    }

    @PostMapping("/decode")
    @ApiOperation("解析token内容")
    public CommonResult<DecodeTokenResponse> decodeToken(@Valid @RequestBody DecodeTokenRequest request) {
        DecodeTokenResponse response = new DecodeTokenResponse();
        response.setUsername(tokenService.decodeToken(request.getToken()));
        return CommonResult.success(response);
    }
}
