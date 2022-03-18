package com.aiit.authority.controller.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserExistRequest implements Serializable {

    @ApiModelProperty(value = "用户名")
    @NotNull(message = "必填字段不能为空")
    private String username;
}
