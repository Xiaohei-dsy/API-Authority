package com.aiit.authority.controller.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteUserRoleResponse implements Serializable {

    @ApiModelProperty("true为成功，其他为失败")
    private Boolean success;
}