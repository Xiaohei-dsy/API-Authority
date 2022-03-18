package com.aiit.authority.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "role")
/**
 * 角色信息表
 */
public class RoleDO {
    /**
     * 自增Id
     */
    private Integer id;
    /**
     * 角色名称
     */
    @TableField("`role_name`")
    private String roleName;
    /**
     * 归属系统
     */
    @TableField("`system_id`")
    private String systemId;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
