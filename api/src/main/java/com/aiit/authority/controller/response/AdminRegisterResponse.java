package com.aiit.authority.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterResponse implements Serializable {
    @ApiModelProperty("请求执行结果")
    private Boolean status;
}
