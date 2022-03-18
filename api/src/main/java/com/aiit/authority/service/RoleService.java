package com.aiit.authority.service;

import com.aiit.authority.controller.request.AddRoleRequest;
import com.aiit.authority.controller.request.DeleteRoleRequest;
import com.aiit.authority.controller.request.ListAllRoleRequest;
import com.aiit.authority.controller.request.UpdateRoleRequest;
import com.aiit.authority.controller.response.AddRoleResponse;
import com.aiit.authority.controller.response.DeleteRoleResponse;
import com.aiit.authority.controller.response.ListRolePageResponse;
import com.aiit.authority.controller.response.UpdateRoleResponse;

public interface RoleService {

    AddRoleResponse insertRole(AddRoleRequest addRoleRequest);

    DeleteRoleResponse deleteRole(DeleteRoleRequest deleteRoleRequest);

    UpdateRoleResponse updateRole(UpdateRoleRequest updateRoleRequest);

    ListRolePageResponse listRole(ListAllRoleRequest listAllRoleRequest);

}
