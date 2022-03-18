package com.aiit.authority.constraint;

import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.aiit.authority.utils.ValidUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameConstraint, String> {


    @Override
    public void initialize(UsernameConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        // 判空 (防止漏传字段)
        if (input == null) {
            throw new AuthenticateException(ResultCodeEnum.EMPTY_PARAMETER);
        }
        // 限制用户名在20个字符以内
        return ValidUtils.isValidName(input, 20);
    }
}
