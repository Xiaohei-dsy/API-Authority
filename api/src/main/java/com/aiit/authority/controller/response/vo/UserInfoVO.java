package com.aiit.authority.controller.response.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO implements Serializable {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("别名")
    private String remarkName;

    @ApiModelProperty("用户种类：管理员：1，普通用户：0")
    private Integer isManager;

    @ApiModelProperty("状态：待审批0，启用1，禁用2")
    private Integer status;
}
