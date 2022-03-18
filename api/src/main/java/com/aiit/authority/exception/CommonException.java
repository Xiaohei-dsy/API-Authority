package com.aiit.authority.exception;

import com.aiit.authority.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonException extends RuntimeException {
    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;


    /**
     * 通过枚举类定义错误信息（可用于任何已知异常）
     */
    public CommonException(ResultCodeEnum resultCodeEnum) {
        this.errorCode = resultCodeEnum.getCode();
        this.errorMsg = resultCodeEnum.getMessage();
    }
}
