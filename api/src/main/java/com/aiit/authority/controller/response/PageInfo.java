package com.aiit.authority.controller.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo implements Serializable {

    @ApiModelProperty("当前页面编号")
    private Long currentPage;

    @ApiModelProperty("总的页数")
    private Long totalPage;

    @ApiModelProperty("每页的记录数")
    private Long pageSize;

    @ApiModelProperty("总体的记录数")
    private Long totalSize;

    @ApiModelProperty("是否有下一页")
    private Boolean hasNext;

    @ApiModelProperty("是否有上一页")
    private Boolean hasPrevious;
}
