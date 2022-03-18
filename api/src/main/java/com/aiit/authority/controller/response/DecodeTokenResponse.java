package com.aiit.authority.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DecodeTokenResponse implements Serializable {

    @ApiModelProperty("token携带的用户名")
    private String username;

}
