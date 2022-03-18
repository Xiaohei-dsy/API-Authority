package com.aiit.authority.utils;

import com.aiit.authority.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    private String code;
    private String message;
    private T result;


    /**
     * 返回成功
     *
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 返回成功结果
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> success(T result) {
        return new CommonResult<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), result);
    }

    /**
     * 返回成功结果，携带信息
     *
     * @param result
     * @param <T>
     * @return
     * @Param message
     */
    public static <T> CommonResult<T> success(T result, String message) {
        return new CommonResult<T>(ResultCodeEnum.SUCCESS.getCode(), message, result);
    }

    /**
     * 传入枚举，返回指定信息
     *
     * @param result
     * @param <T>
     * @return
     * @Param message
     */
    public static <T> CommonResult<T> success(T result, ResultCodeEnum resultCode) {
        return new CommonResult<T>(resultCode.getCode(), resultCode.getMessage(),result);
    }

    /**
     * 失败返回结果
     *
     * @param resultCode 结果状态码
     */
    public static <T> CommonResult<T> fail(ResultCodeEnum resultCode) {
        return new CommonResult<T>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 失败返回错误码，和指定信息
     *
     * @param resultCode 结果状态码
     * @param message   错误信息
     */
    public static <T> CommonResult<T> fail(ResultCodeEnum resultCode, String message) {
        return new CommonResult<T>(resultCode.getCode(), message, null);
    }

    /**
     * @param resultCode 结果状态码
     * @param message   错误信息
     */
    public static <T> CommonResult<T> fail(String resultCode, String message) {
        return new CommonResult<T>(resultCode, message, null);
    }


}


