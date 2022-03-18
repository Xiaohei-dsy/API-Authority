package com.aiit.authority.controller.response;

import com.aiit.authority.controller.response.vo.ResourceVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListResourceResponse extends ListBaseResponse implements Serializable {

    @ApiModelProperty("返回的资源列表")
    private List<ResourceVO> resourceList;
}
