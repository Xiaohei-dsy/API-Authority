package com.aiit.authority.manager.dto;

import java.util.List;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.repository.entity.SystemDO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListSystemDTO {

    @ApiModelProperty("系统分页page信息")
    private PageInfo pageInfo;

    @ApiModelProperty("系统分页库表信息")
    private List<SystemDO> list;

}
