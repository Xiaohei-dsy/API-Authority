package com.aiit.authority.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "role_resource")
public class RoleResourceDO extends BaseDO {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 系统名称
     */
    private String systemId;

    /**
     * 资源id
     */
    private String resourceId;

}