package com.aiit.authority.manager;

import com.aiit.authority.enums.UserPositionEnum;
import com.aiit.authority.enums.UserStatusEnum;
import com.aiit.authority.manager.dto.ListUsersDTO;
import com.aiit.authority.repository.entity.UserDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

public interface UserManager {

    Boolean insertUser(String username, String password, String remarkName,
                       UserPositionEnum role, UserStatusEnum status);

    ListUsersDTO listUsers(Integer currentPage, Integer pageSize, QueryWrapper<UserDO> wrapper);

    List<UserDO> listUsers(QueryWrapper wrapper);

    Boolean updateUser(String username, UserStatusEnum status);

    Boolean deleteUser(String username);

    UserDO getUser(String username);

}
