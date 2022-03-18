package com.aiit.authority.constraint;

import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.aiit.authority.utils.ValidUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoleNameValidator implements ConstraintValidator<RoleConstraint, String> {

    @Override
    public void initialize(RoleConstraint cronConstraint) {
    }

    @Override
    public boolean isValid(String roleName, ConstraintValidatorContext context) {
        // 判空 (防止漏传字段)
        if (roleName == null) {
            throw new AuthenticateException(ResultCodeEnum.EMPTY_PARAMETER);
        }
        /**
         * 匹配由中文、数字、26个英文字母（不缺分大小写）或者下划线组成的指定长度字符串，不能以下划线开头或结尾。
         */
        return ValidUtils.isValidName(roleName, 10);
    }
}