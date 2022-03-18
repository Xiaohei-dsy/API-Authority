package com.aiit.authority.controller.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserRoleRequest implements Serializable {

    @ApiModelProperty(value = "用户名称")
    private String userName;
}