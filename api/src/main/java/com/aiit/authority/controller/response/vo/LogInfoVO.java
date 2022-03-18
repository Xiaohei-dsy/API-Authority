package com.aiit.authority.controller.response.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInfoVO implements Serializable {

    @ApiModelProperty("操作时间")
    private String operationTime;

    @ApiModelProperty("操作种类：0插入，1更新，2删除")
    private Integer operation;

    @ApiModelProperty("操作详情")
    private String record;

    @ApiModelProperty("操作人")
    private String operator;

}
