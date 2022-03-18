package com.aiit.authority.handler;

import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.aiit.authority.exception.CommonException;
import com.aiit.authority.exception.DatabaseException;
import com.aiit.authority.exception.ServiceException;
import com.aiit.authority.utils.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@ControllerAdvice
/**
 * RestControllerAdvice会自动帮助catch,并匹配相应的ExceptionHandler,
 * 然后重新封装异常信息，返回值，统一格式返回给前端。
 * 并且具有ResponseBody的功能，将方法的返回值，以特定的格式写入到response的body区域，
 * 进而将数据返回给客户端。当方法上面没有写ResponseBody,底层会将方法的返回值封装为ModelAndView对象。
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //捕获校验注解抛出的异常，并处理异常信息返回前端。
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 然后提取错误提示信息进行返回
        String errorMsg = objectError.getDefaultMessage();
        return CommonResult.fail(ResultCodeEnum.ILLEGAL_PARAM, errorMsg);
    }

    // 捕获自定义异常，处理并直接返回结果至前端
    @ExceptionHandler(DatabaseException.class)
    public CommonResult databaseExceptionHandler(DatabaseException e) {
        return commonExceptionHandler(e);
    }

    // 捕获自定义异常，处理并直接返回结果至前端
    @ExceptionHandler(ServiceException.class)
    public CommonResult serviceExceptionHandler(DatabaseException e) {
        return commonExceptionHandler(e);
    }

    // 捕获自定义token验证异常，处理并直接返回结果至前端
    @ExceptionHandler(AuthenticateException.class)
    public CommonResult authenticateExceptionHandler(AuthenticateException e) {
        return commonExceptionHandler(e);
    }


    public CommonResult commonExceptionHandler(CommonException e) {
        // log.error携带第二个参数（e）时可打印详细信息，此处为了简洁省略
        log.error(e.getErrorMsg(), e);
        return CommonResult.fail(e.getErrorCode(), e.getErrorMsg());
    }


}
