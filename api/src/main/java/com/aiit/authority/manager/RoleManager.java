package com.aiit.authority.manager;

import com.aiit.authority.manager.dto.ListRoleDTO;
import com.aiit.authority.repository.entity.RoleDO;

public interface RoleManager {

    Boolean insertRole(String roleName, String systemId, String description);

    RoleDO getRole(String roleName, String systemId);

    Boolean deleteRole(String roleName, String systemId);

    Boolean updateRole(String roleName, String systemId, String description);

    ListRoleDTO listRole(Integer currentPage, Integer pageSize);

}
