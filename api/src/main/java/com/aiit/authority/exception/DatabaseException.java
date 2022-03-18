package com.aiit.authority.exception;

import com.aiit.authority.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DatabaseException extends CommonException {


    /**
     * 通过枚举类定义错误信息（可用于任何已知异常）
     *
     * @param resultCodeEnum
     */
    public DatabaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum);
    }
}
