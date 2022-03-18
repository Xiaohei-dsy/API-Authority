package com.aiit.authority.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListResourceRequest extends ListBaseRequest implements Serializable {

    @ApiModelProperty("系统名称,可以不传")
    private String systemId;
}
