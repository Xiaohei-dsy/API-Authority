package com.aiit.authority.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfoDTO {

    @ApiModelProperty("是否通过token校验")
    private Boolean valid;

    @ApiModelProperty("去掉前缀的token")
    private String rawToken;

    @ApiModelProperty("token包含用户名")
    private String username;
}
