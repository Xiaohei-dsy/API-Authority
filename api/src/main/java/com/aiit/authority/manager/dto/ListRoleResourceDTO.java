package com.aiit.authority.manager.dto;

import java.util.List;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.repository.entity.RoleResourceDO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListRoleResourceDTO {

    @ApiModelProperty("角色资源分页page信息")
    private PageInfo pageInfo;

    @ApiModelProperty("角色资源分页库表信息")
    private List<RoleResourceDO> list;

}