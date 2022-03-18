package com.aiit.authority.controller.request;

import com.aiit.authority.constraint.SelfOperationConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateUserRequest implements Serializable {

    @ApiModelProperty("用户名")
    @SelfOperationConstraint
    private String username;
}
