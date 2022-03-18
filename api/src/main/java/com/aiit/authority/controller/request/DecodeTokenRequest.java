package com.aiit.authority.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DecodeTokenRequest implements Serializable {

    @ApiModelProperty("token")
    @NotNull(message = "必填字段为空")
    @NotBlank(message = "token不可为空字符串")
    private String token;

}
