package com.aiit.authority.manager.dto;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.repository.entity.ResourceDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListResourceDTO {

    @ApiModelProperty("分页page信息")
    private PageInfo pageInfo;

    @ApiModelProperty("资源列表")
    private List<ResourceDO> resourceList;
}
