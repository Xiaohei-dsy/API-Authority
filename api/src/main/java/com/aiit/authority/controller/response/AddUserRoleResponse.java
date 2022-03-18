package com.aiit.authority.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRoleResponse implements Serializable {

    @ApiModelProperty("true为成功，其他为失败")
    private Boolean success;
}
