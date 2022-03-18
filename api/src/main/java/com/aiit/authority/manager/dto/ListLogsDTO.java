package com.aiit.authority.manager.dto;

import com.aiit.authority.controller.response.PageInfo;
import com.aiit.authority.repository.entity.LogDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListLogsDTO {

    @ApiModelProperty("日志列表分页page信息")
    private PageInfo pageInfo;

    @ApiModelProperty("日志列表分页信息")
    private List<LogDO> list;
}
