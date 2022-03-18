package com.aiit.authority.exception;

import com.aiit.authority.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticateException extends CommonException {

    /**
     * 通过枚举类定义错误信息（可用于任何已知异常）
     *
     * @param resultCodeEnum
     */
    public AuthenticateException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum);
    }

}
