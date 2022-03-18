package com.aiit.authority.controller.response;

import java.io.Serializable;
import java.util.List;

import com.aiit.authority.controller.response.vo.RoleInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListRolePageResponse extends ListBaseResponse implements Serializable {

    @ApiModelProperty("返回的角色列表")
    private List<RoleInfoVO> userInfoList;
}