package com.aiit.authority.controller.response;

import java.io.Serializable;
import java.util.List;

import com.aiit.authority.controller.response.vo.UserRoleVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserRoleListResponse extends ListBaseResponse implements Serializable {

    @ApiModelProperty(value = "用户角色分页信息")
    private List<UserRoleVO> userRoleVOList;

}
