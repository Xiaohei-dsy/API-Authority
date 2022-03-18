package com.aiit.authority.constraint;

import com.aiit.authority.utils.JsonUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JsonValidator implements ConstraintValidator<JsonConstraint, String> {

    @Override
    public void initialize(JsonConstraint cronConstraint) {
    }

    @Override
    public boolean isValid(String json, ConstraintValidatorContext context) {
        return StringUtils.isEmpty(json) || JsonUtils.isJson(json);
    }
}
