package com.aiit.authority.controller.request;

import java.io.Serializable;

import com.aiit.authority.constraint.RoleConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleRequest implements Serializable {

    @ApiModelProperty(value = "角色名称")
    @RoleConstraint
    private String roleName;

    @ApiModelProperty(value = "归属系统")
    private String systemId;

    @ApiModelProperty(value = "角色描述")
    private String description;
}
