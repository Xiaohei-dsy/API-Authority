package com.aiit.authority.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddResourceResponse implements Serializable {

    @ApiModelProperty("是否添加成功,true:成功，其他失败")
    private Boolean success;
}
