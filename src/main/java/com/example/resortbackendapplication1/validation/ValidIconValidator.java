package com.example.resortbackendapplication1.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidIconValidator implements ConstraintValidator<ValidIcon, IconCarrier> {

    @Override
    public boolean isValid(IconCarrier carrier, ConstraintValidatorContext context) {
        if (carrier.getIconType() == null) {
            return true;
        }
        boolean valid = carrier.getIconValue() != null && !carrier.getIconValue().isBlank();
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("icon_value must not be blank when icon_type is provided")
                    .addPropertyNode("icon_value")
                    .addConstraintViolation();
        }
        return valid;
    }
}
