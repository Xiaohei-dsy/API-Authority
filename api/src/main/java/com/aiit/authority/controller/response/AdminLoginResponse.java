package com.aiit.authority.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginResponse implements Serializable {

    @ApiModelProperty("登陆成功后生成的token")
    private String token;

}
