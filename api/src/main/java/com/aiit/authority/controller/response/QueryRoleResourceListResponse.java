package com.aiit.authority.controller.response;

import java.io.Serializable;
import java.util.List;

import com.aiit.authority.controller.response.vo.RoleResourceVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryRoleResourceListResponse implements Serializable {

    @ApiModelProperty(value = "角色资源分页信息")
    private List<RoleResourceVO> roleResourceVOList;

    @ApiModelProperty("页面信息")
    private PageInfo page;

}