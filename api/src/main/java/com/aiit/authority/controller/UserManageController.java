package com.aiit.authority.controller;

import com.aiit.authority.controller.request.QueryUserExistRequest;
import com.aiit.authority.controller.request.UserLoginRequest;
import com.aiit.authority.controller.request.UserLogoutRequest;
import com.aiit.authority.controller.request.UserRegisterRequest;
import com.aiit.authority.controller.response.QueryUserExistResponse;
import com.aiit.authority.controller.response.UserLoginResponse;
import com.aiit.authority.controller.response.UserLogoutResponse;
import com.aiit.authority.controller.response.UserRegisterResponse;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.service.ManageService;
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
@RequestMapping("/authority/manage/user")
@Api(tags = {"用户相关操作"})
public class UserManageController {

    @Resource
    ManageService manageService;

    @PostMapping("/login")
    @ApiOperation("员工登录")
    public CommonResult<UserLoginResponse> userLogin(@Valid @RequestBody UserLoginRequest request) {
        return CommonResult.success(manageService.userLogin(request), ResultCodeEnum.USER_LOGIN_SUCCESS);
    }

    @PostMapping("/logout")
    @ApiOperation("员工登出")
    public CommonResult<UserLogoutResponse> userLogin(@Valid @RequestBody UserLogoutRequest request) {
        return CommonResult.success(manageService.userLogout(request), ResultCodeEnum.USER_LOGOUT_SUCCESS);
    }

    @PostMapping("/register")
    @ApiOperation("普通用户注册")
    public CommonResult<UserRegisterResponse> adminLogin(@Valid @RequestBody UserRegisterRequest request) {
        return CommonResult.success(manageService.registerUser(request), ResultCodeEnum.USER_REGISTER_SUCCESS);
    }

    @PostMapping("/query/exist")
    @ApiOperation("查询用户是否存在")
    public CommonResult<QueryUserExistResponse> queryIfUserExist(@Valid @RequestBody QueryUserExistRequest request) {
        return CommonResult.success(manageService.queryIfUserExist(request));
    }

}
