package com.aiit.authority.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;



@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = RoleNameValidator.class)
@Documented
public @interface RoleConstraint {

    String message() default "角色名格式错误：长度10的字母/数字/下划线组合，不能以下划线开头或结尾。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}