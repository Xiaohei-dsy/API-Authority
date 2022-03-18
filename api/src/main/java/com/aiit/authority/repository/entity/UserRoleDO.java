package com.aiit.authority.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_role")
public class UserRoleDO extends BaseDO {

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 系统名称
     */
    private String systemId;

}
