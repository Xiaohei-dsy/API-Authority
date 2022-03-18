package com.aiit.authority.manager;

import com.aiit.authority.manager.dto.ListUserRoleDTO;
import com.aiit.authority.repository.entity.UserRoleDO;

public interface UserRoleManager {

    Boolean insertRole(String username, String roleName, String systemId);

    ListUserRoleDTO listUserRoleByUser(String username, Integer currentPage, Integer pageSize);

    ListUserRoleDTO listUserRoleBySystem(String roleName, String systemId, Integer currentPage, Integer pageSize);

    Boolean deleteUserRole(String username, String roleName, String systemId);

    UserRoleDO getUserRole(String username, String systemId);

    Boolean updateUserRole(String username, String roleName, String systemId);

    Boolean isValidUserRole(String username, String roleName, String systemId);

}
