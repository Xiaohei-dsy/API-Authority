package com.aiit.authority.manager;

import com.aiit.authority.manager.dto.ListRoleResourceDTO;
import com.aiit.authority.repository.entity.RoleResourceDO;

public interface RoleResourceManager {

    Boolean insertRoleResource(String roleName, String systemId ,String resourceId);

    ListRoleResourceDTO listRoleResource(String roleName, String systemId, Integer currentPage, Integer pageSize);

    Boolean deleteRoleResource(String roleName, String systemId, String resourceId);

    RoleResourceDO getRoleResource(String roleName, String systemId, String resourceId);

    Integer countResource(String roleName, String systemId);

}
