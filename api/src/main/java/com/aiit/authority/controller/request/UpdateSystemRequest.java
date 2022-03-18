package com.aiit.authority.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSystemRequest implements Serializable {

    @ApiModelProperty(value = "系统id")
    private String systemId;

    @ApiModelProperty(value = "系统描述")
    private String description;

}