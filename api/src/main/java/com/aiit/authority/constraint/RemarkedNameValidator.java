package com.aiit.authority.constraint;

import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.aiit.authority.utils.ValidUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RemarkedNameValidator implements ConstraintValidator<RemarkedNameConstraint, String> {
    @Override
    public void initialize(RemarkedNameConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        // 判空 (防止漏传字段)
        if (input == null) {
            throw new AuthenticateException(ResultCodeEnum.EMPTY_PARAMETER);
        }
        // 可以为空，如果非空，限制别名在20个字符以内
        return input.length() == 0 || ValidUtils.isValidName(input, 20);
    }
}
