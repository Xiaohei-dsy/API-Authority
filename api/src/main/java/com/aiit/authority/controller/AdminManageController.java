package com.aiit.authority.controller;

import com.aiit.authority.controller.request.ActivateUserRequest;
import com.aiit.authority.controller.request.AdminLoginRequest;
import com.aiit.authority.controller.request.AdminLogoutRequest;
import com.aiit.authority.controller.request.AdminRegisterRequest;
import com.aiit.authority.controller.request.AdminRegisterUserRequest;
import com.aiit.authority.controller.request.DeleteUserRequest;
import com.aiit.authority.controller.request.DisableUserRequest;
import com.aiit.authority.controller.request.ListAllLogsRequest;
import com.aiit.authority.controller.request.ListAllUsersRequest;
import com.aiit.authority.controller.request.ListPendingUsersRequest;
import com.aiit.authority.controller.request.ListReviewedUsersRequest;
import com.aiit.authority.controller.response.ActivateUserResponse;
import com.aiit.authority.controller.response.AdminLoginResponse;
import com.aiit.authority.controller.response.AdminLogoutResponse;
import com.aiit.authority.controller.response.AdminRegisterResponse;
import com.aiit.authority.controller.response.AdminRegisterUserResponse;
import com.aiit.authority.controller.response.DeleteUserResponse;
import com.aiit.authority.controller.response.DisableUserResponse;
import com.aiit.authority.controller.response.ListAllLogsResponse;
import com.aiit.authority.controller.response.ListAllUsersResponse;
import com.aiit.authority.controller.response.ListApprovedUsersResponse;
import com.aiit.authority.controller.response.ListPendingUsersResponse;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.service.LogService;
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

/**
 * 除登录、登出、注册以外的接口，全部经过TokenInterceptor和AdminInterceptor拦截器
 * LoginInterceptor用于登录验证和token续签
 *
 * @see com.aiit.authority.interceptor.TokenInterceptor
 * AdminInterceptor用于防止user跨级调用admin
 * @see com.aiit.authority.interceptor.AdminInterceptor
 */
@RestController
@RequestMapping("/authority/manage/admin")
@Api(tags = {"管理员相关操作"})
public class AdminManageController {

    @Resource
    private ManageService manageService;

    @Resource
    private LogService logService;

    @PostMapping("/login")
    @ApiOperation("管理员登录")
    public CommonResult<AdminLoginResponse> adminLogin(@Valid @RequestBody AdminLoginRequest request) {
        return CommonResult.success(manageService.adminLogin(request), ResultCodeEnum.ADMIN_LOGIN_SUCCESS);
    }

    @PostMapping("/logout")
    @ApiOperation("管理员登出")
    public CommonResult<AdminLogoutResponse> adminLogin(@Valid @RequestBody AdminLogoutRequest request) {
        return CommonResult.success(manageService.adminLogout(request), ResultCodeEnum.ADMIN_LOGOUT_SUCCESS);
    }

    @PostMapping("/register")
    @ApiOperation("管理员注册")
    public CommonResult<AdminRegisterResponse> adminRegister(@Valid @RequestBody AdminRegisterRequest request) {
        AdminRegisterResponse response = manageService.registerAdmin(request);
        return CommonResult.success(response, ResultCodeEnum.ADMIN_REGISTER_SUCCESS);
    }

    @PostMapping("/user/query/all")
    @ApiOperation("管理员查询全部用户")
    public CommonResult<ListAllUsersResponse> listAllUsers(@Valid @RequestBody ListAllUsersRequest request) {
        return CommonResult.success(manageService.listAllUsers(request));
    }

    @PostMapping("/user/query/pending")
    @ApiOperation("管理员查询待处理用户")
    public CommonResult<ListPendingUsersResponse> listPendingUsers(
            @Valid @RequestBody ListPendingUsersRequest request) {
        return CommonResult.success(manageService.listPendingUsers(request));
    }

    @PostMapping("/user/query/approved")
    @ApiOperation("管理员查询已审批用户")
    public CommonResult<ListApprovedUsersResponse> listApprovedUsers(
            @Valid @RequestBody ListReviewedUsersRequest request) {
        return CommonResult.success(manageService.listReviewedUsers(request));
    }

    @PostMapping("/user/activate")
    @ApiOperation("管理员激活用户")
    public CommonResult<ActivateUserResponse> activateUser(@Valid @RequestBody ActivateUserRequest request) {
        ActivateUserResponse response = manageService.activateUser(request);
        return CommonResult.success(response, ResultCodeEnum.ACTIVATE_USER_SUCCESS);
    }

    @PostMapping("/user/create")
    @ApiOperation("管理员创建用户")
    public CommonResult<AdminRegisterUserResponse> registerUserByAdmin(
            @Valid @RequestBody AdminRegisterUserRequest request) {
        return CommonResult.success(manageService.registerUserByAdmin(request), ResultCodeEnum.USER_REGISTER_SUCCESS);
    }

    @PostMapping("/user/delete")
    @ApiOperation("管理员删除用户")
    public CommonResult<DeleteUserResponse> registerUserByAdmin(@Valid @RequestBody DeleteUserRequest request) {
        return CommonResult.success(manageService.deleteUser(request), ResultCodeEnum.DELETE_USER_SUCCESS);
    }

    @PostMapping("/user/disable")
    @ApiOperation("管理员禁用用户")
    public CommonResult<DisableUserResponse> registerUserByAdmin(@Valid @RequestBody DisableUserRequest request) {
        return CommonResult.success(manageService.disableUser(request), ResultCodeEnum.DISABLE_USER_SUCCESS);
    }

    @PostMapping("/logs/query/all")
    @ApiOperation("管理员查询全部日志")
    public CommonResult<ListAllLogsResponse> registerUserByAdmin(@Valid @RequestBody ListAllLogsRequest request) {
        return CommonResult.success(logService.listAllLogs(request));
    }


}
