package com.aiit.authority.service;

import com.aiit.authority.controller.request.ActivateUserRequest;
import com.aiit.authority.controller.request.AdminLoginRequest;
import com.aiit.authority.controller.request.AdminLogoutRequest;
import com.aiit.authority.controller.request.AdminRegisterRequest;
import com.aiit.authority.controller.request.AdminRegisterUserRequest;
import com.aiit.authority.controller.request.DeleteUserRequest;
import com.aiit.authority.controller.request.DisableUserRequest;
import com.aiit.authority.controller.request.ListAllUsersRequest;
import com.aiit.authority.controller.request.ListPendingUsersRequest;
import com.aiit.authority.controller.request.ListReviewedUsersRequest;
import com.aiit.authority.controller.request.QueryUserExistRequest;
import com.aiit.authority.controller.request.UserLoginRequest;
import com.aiit.authority.controller.request.UserLogoutRequest;
import com.aiit.authority.controller.request.UserRegisterRequest;
import com.aiit.authority.controller.response.ActivateUserResponse;
import com.aiit.authority.controller.response.AdminLoginResponse;
import com.aiit.authority.controller.response.AdminLogoutResponse;
import com.aiit.authority.controller.response.AdminRegisterResponse;
import com.aiit.authority.controller.response.AdminRegisterUserResponse;
import com.aiit.authority.controller.response.DeleteUserResponse;
import com.aiit.authority.controller.response.DisableUserResponse;
import com.aiit.authority.controller.response.ListAllUsersResponse;
import com.aiit.authority.controller.response.ListApprovedUsersResponse;
import com.aiit.authority.controller.response.ListPendingUsersResponse;
import com.aiit.authority.controller.response.QueryUserExistResponse;
import com.aiit.authority.controller.response.UserLoginResponse;
import com.aiit.authority.controller.response.UserLogoutResponse;
import com.aiit.authority.controller.response.UserRegisterResponse;

public interface ManageService {

    UserRegisterResponse registerUser(UserRegisterRequest request);

    AdminRegisterResponse registerAdmin(AdminRegisterRequest request);

    ListAllUsersResponse listAllUsers(ListAllUsersRequest request);

    ListPendingUsersResponse listPendingUsers(ListPendingUsersRequest request);

    ListApprovedUsersResponse listReviewedUsers(ListReviewedUsersRequest request);

    ActivateUserResponse activateUser(ActivateUserRequest request);

    AdminRegisterUserResponse registerUserByAdmin(AdminRegisterUserRequest request);

    DeleteUserResponse deleteUser(DeleteUserRequest request);

    DisableUserResponse disableUser(DisableUserRequest request);

    UserLoginResponse userLogin(UserLoginRequest request);

    AdminLoginResponse adminLogin(AdminLoginRequest request);

    UserLogoutResponse userLogout(UserLogoutRequest request);

    AdminLogoutResponse adminLogout(AdminLogoutRequest request);

    QueryUserExistResponse queryIfUserExist(QueryUserExistRequest request);

}
