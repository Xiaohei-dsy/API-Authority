package com.aiit.authority.controller.response.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoVO implements Serializable {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("系统名称")
    private String systemId;

    @ApiModelProperty("功能描述")
    private String description;

}