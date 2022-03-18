package com.aiit.authority.controller.request;

import com.aiit.authority.constraint.JsonConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddResourceRequest implements Serializable {

    @NotNull
    @ApiModelProperty("资源Id")
    private String resourceId;

    @NotNull
    @ApiModelProperty("资源名称")
    private String resourceName;

    @NotNull
    @ApiModelProperty("系统Id")
    private String systemId;

    @ApiModelProperty("资源详情,若存在，一定为json形式")
    @JsonConstraint
    private String resourceDetail;

    @ApiModelProperty("资源类型，用来解析 resourceDetail")
    private Integer resourceType;

    @ApiModelProperty("资源描述")
    @NotNull
    private String description;
}
