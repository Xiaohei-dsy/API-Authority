package com.aiit.authority.controller.request;

import java.io.Serializable;

import com.aiit.authority.constraint.SystemConstraint;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSystemRequest implements Serializable {

    @ApiModelProperty(value = "系统名称")
    @SystemConstraint
    private String systemId;

    @ApiModelProperty(value = "功能描述")
    private String description;

}
