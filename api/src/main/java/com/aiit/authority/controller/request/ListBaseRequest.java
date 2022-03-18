package com.aiit.authority.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListBaseRequest implements Serializable {

    @ApiModelProperty("当前页号")
    @Min(value = 1, message = "页号不能小于1")
    @NotNull(message = "必填字段不能为空")
    private Integer currentPage;

    @ApiModelProperty("每页大小")
    @Min(value = 1, message = "每页数量不能少于1")
    @Max(value = 20,message = "每页数量不能大于20")
    @NotNull(message = "必填字段不能为空")
    private Integer pageSize;
}
