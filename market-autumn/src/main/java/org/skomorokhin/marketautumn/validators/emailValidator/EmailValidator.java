package org.skomorokhin.marketautumn.validators.emailValidator;

import org.skomorokhin.marketautumn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailValidator implements ConstraintValidator<ValidMailConstraint, String> {

    @Autowired
    private UserService service;

    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        pattern = Pattern.compile(EMAIL_PATTERN);
        if (email == null) {
            return false;
        }
        matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            context
                    .buildConstraintViolationWithTemplate("email is incorrect")
                    .addConstraintViolation();
        }
        if (service.existByEmail(email)) {
            context
                    .buildConstraintViolationWithTemplate("is exist")
                    .addConstraintViolation();
            return false;
        }

        return matcher.matches();
    }
}
