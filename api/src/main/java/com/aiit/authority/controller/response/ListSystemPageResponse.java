package com.aiit.authority.controller.response;

import java.io.Serializable;
import java.util.List;

import com.aiit.authority.controller.response.vo.SystemInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListSystemPageResponse extends ListBaseResponse implements Serializable {

    @ApiModelProperty("系统列表")
    private List<SystemInfoVO> systemInfoList;

}
