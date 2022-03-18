package com.aiit.authority.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListBaseResponse implements Serializable {

    @ApiModelProperty("页面信息")
    private PageInfo page;

}
