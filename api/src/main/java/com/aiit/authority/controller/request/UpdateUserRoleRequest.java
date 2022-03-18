package com.aiit.authority.controller.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleRequest implements Serializable {

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "修改后的角色名称")
    private String roleName;

    @ApiModelProperty(value = "系统ID")
    private String systemId;
}