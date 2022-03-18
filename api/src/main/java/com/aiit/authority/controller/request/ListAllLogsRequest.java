package com.aiit.authority.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListAllLogsRequest extends ListBaseRequest implements Serializable {

    @ApiModelProperty("操作者")
    private String operator;

    @ApiModelProperty("操作种类")
    @Range(min = 0, max = 2, message = "操作种类仅支持传入0,1,2")
    private Integer operation;

    @ApiModelProperty("按时间展示顺序：0最新优先，1最旧优先")
    @Range(min = 0, max = 1, message = "升降序仅支持传入0,1")
    private Integer timeOrder;
}
