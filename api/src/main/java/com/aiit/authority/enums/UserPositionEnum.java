package com.aiit.authority.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserPositionEnum {

    NORMAL_USER(0, "普通用户"),
    ADMIN(1, "管理员");

    private Integer value;

    private String description;
}
