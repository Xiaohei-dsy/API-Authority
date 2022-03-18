package com.aiit.authority.controller.request;


import com.aiit.authority.constraint.PasswordConstraint;
import com.aiit.authority.constraint.RemarkedNameConstraint;
import com.aiit.authority.constraint.UsernameConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegisterUserRequest implements Serializable {

    @ApiModelProperty("用户名")
    @UsernameConstraint
    private String username;

    @ApiModelProperty("密码")
    @PasswordConstraint
    private String password;

    @ApiModelProperty("别名")
    @RemarkedNameConstraint
    private String remarkName;
}
