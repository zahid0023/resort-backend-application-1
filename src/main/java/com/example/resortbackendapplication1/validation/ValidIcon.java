package com.example.resortbackendapplication1.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidIconValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIcon {
    String message() default "icon_value must not be blank when icon_type is provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
