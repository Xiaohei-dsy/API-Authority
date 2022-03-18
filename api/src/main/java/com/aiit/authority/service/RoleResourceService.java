package com.aiit.authority.service;

import com.aiit.authority.controller.request.AddRoleResourceRequest;
import com.aiit.authority.controller.request.DeleteRoleResourceRequest;
import com.aiit.authority.controller.request.QueryRoleResourceListRequest;
import com.aiit.authority.controller.response.AddRoleResourceResponse;
import com.aiit.authority.controller.response.DeleteRoleResourceResponse;
import com.aiit.authority.controller.response.QueryRoleResourceListResponse;

public interface RoleResourceService {

    AddRoleResourceResponse insertRoleResource(AddRoleResourceRequest request);

    DeleteRoleResourceResponse deleteRoleResource(DeleteRoleResourceRequest request);

    QueryRoleResourceListResponse listRoleResource(QueryRoleResourceListRequest request);

}
