package com.aiit.authority.controller.response.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceVO implements Serializable {

    @ApiModelProperty("资源唯一ID")
    private String resourceId;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("资源描述")
    private String description;

    @ApiModelProperty("系统ID")
    private String systemId;

    @ApiModelProperty("资源内容，前端不展示")
    private String resourceDetail;

    @ApiModelProperty("资源类型,前端不展示")
    private Integer resourceType;
}
