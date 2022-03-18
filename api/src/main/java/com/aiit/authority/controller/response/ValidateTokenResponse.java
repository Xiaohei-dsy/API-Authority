package com.aiit.authority.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ValidateTokenResponse implements Serializable {

    @ApiModelProperty("token是否合法，合法为true，非法为false")
    private Boolean isValid;
}
