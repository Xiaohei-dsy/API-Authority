package com.aiit.authority.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * redis存储前缀，区分同一用户存储的不同字段
 */
@Getter
@AllArgsConstructor
public enum RedisPreFixEnum {

    TOKEN("t_", "token"),
    ROLE("r_", "用户种类"),
    SAVE_TIME("st_", "存储时间");

    private String prefix;

    private String description;
}
