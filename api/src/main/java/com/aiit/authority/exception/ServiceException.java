package com.aiit.authority.exception;

import com.aiit.authority.enums.ResultCodeEnum;

public class ServiceException extends CommonException {

    /**
     * 通过枚举类定义错误信息（可用于任何已知异常）
     *
     * @param resultCodeEnum
     */
    public ServiceException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum);
    }
}
