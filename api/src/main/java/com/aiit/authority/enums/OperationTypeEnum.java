package com.aiit.authority.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationTypeEnum {

    INSERT(0,"数据库插入操作"),
    UPDATE(1,"数据库更新操作"),
    DELETE(2,"数据库删除操作");

    private Integer type;

    private String description;
}
