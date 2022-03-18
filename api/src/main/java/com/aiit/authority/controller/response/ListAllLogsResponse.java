package com.aiit.authority.controller.response;

import com.aiit.authority.controller.response.vo.LogInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListAllLogsResponse extends ListBaseResponse implements Serializable {

    @ApiModelProperty("日志信息列表")
    private List<LogInfoVO> logInfoList;

}
