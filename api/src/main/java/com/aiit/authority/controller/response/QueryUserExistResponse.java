package com.aiit.authority.controller.response;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserExistResponse implements Serializable {

    @ApiModelProperty(value = "用户是否存在: true代表存在，false代表不存在")
    private Boolean exist;
}
