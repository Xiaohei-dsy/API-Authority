package com.aiit.authority.service;

import com.aiit.authority.controller.request.*;
import com.aiit.authority.controller.response.AddUserRoleResponse;
import com.aiit.authority.controller.response.DeleteUserRoleResponse;
import com.aiit.authority.controller.response.QueryUserRoleListResponse;
import com.aiit.authority.controller.response.UpdateUserRoleResponse;

public interface UserRoleService {

    AddUserRoleResponse insertUserRole(AddUserRoleRequest request);

    DeleteUserRoleResponse deleteUserRole(DeleteUserRoleRequest request);

    QueryUserRoleListResponse listRoleUser(QueryUserBySystemRequest request);

    QueryUserRoleListResponse listUserRole(QueryUserRoleListRequest request);

    UpdateUserRoleResponse updateUserRole(UpdateUserRoleRequest request);

}
