package com.aiit.authority.constraint;

import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.aiit.authority.utils.ValidUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Override
    public void initialize(PasswordConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        // 判空 (防止漏传字段)
        if (input == null) {
            throw new AuthenticateException(ResultCodeEnum.EMPTY_PARAMETER);
        }
        // 限制密码在6-15个字符长度
        return ValidUtils.isValidPassword(input, 15, 6);
    }
}
