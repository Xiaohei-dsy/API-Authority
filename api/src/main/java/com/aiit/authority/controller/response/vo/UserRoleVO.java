package com.aiit.authority.controller.response.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleVO implements Serializable {

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "系统ID")
    private String systemId;

}
