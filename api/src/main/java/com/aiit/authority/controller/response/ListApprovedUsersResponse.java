package com.aiit.authority.controller.response;

import com.aiit.authority.controller.response.vo.UserInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListApprovedUsersResponse extends ListBaseResponse implements Serializable {

    @ApiModelProperty("返回的用户列表")
    private List<UserInfoVO> userInfoList;

}
