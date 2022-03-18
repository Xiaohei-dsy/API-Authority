package com.aiit.authority.manager.dto;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.repository.entity.UserDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListUsersDTO {

    @ApiModelProperty("用户列表分页page信息")
    private PageInfo pageInfo;

    @ApiModelProperty("用户列表分页信息")
    private List<UserDO> list;
}
