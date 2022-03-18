package com.aiit.authority.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    WAITING_APPROVAL(0, "等待处理"),
    ACTIVE_USER(1, "使用中"),
    BANNED_USER(2, "禁用");

    private Integer value;

    private String description;

}
