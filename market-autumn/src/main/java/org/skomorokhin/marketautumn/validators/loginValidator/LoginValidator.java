package org.skomorokhin.marketautumn.validators.loginValidator;

import org.skomorokhin.marketautumn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<ValidLoginConstraint, String> {

    @Autowired
    UserService userService;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        if (userService.existsByLogin(login)) {
            return false;
        }
        return true;
    }
}
