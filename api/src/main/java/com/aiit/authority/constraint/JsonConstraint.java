package com.aiit.authority.constraint;

import javax.validation.Payload;

public @interface JsonConstraint {

    String message() default "资源详情格式不合法,需要为json格式";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
