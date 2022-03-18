package com.aiit.authority.constraint;

import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.aiit.authority.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class SelfOperationValidator implements ConstraintValidator<SelfOperationConstraint, String> {


    @Override
    public void initialize(SelfOperationConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {

        // 判空 (防止漏传字段)
        if (input == null) {
            throw new AuthenticateException(ResultCodeEnum.EMPTY_PARAMETER);
        }
        // 获取token并解析出username
        boolean pass = !input.equals(ThreadLocalUtil.get("username"));
        log.info("自操作参数校验通过");
        return pass;
    }
}
