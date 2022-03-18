package com.aiit.authority.constraint;

import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.aiit.authority.utils.ValidUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SystemNameValidator implements ConstraintValidator<SystemConstraint, String> {

    @Override
    public void initialize(SystemConstraint cronConstraint) {
    }

    @Override
    public boolean isValid(String systemId, ConstraintValidatorContext context) {
        // 判空 (防止漏传字段)
        if (systemId == null) {
            throw new AuthenticateException(ResultCodeEnum.EMPTY_PARAMETER);
        }
        /**
         * 通过正则表达式验证参数：由数字、26个英文字母(不区分大小写)或者下划线组成的长度为15及以内的字符串。
         */
        return ValidUtils.isValidField(systemId, 15);
    }
}