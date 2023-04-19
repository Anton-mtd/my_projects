package org.skomorokhin.marketautumn.dto;

import lombok.Data;
import org.skomorokhin.marketautumn.validators.emailValidator.ValidMailConstraint;
import org.skomorokhin.marketautumn.validators.loginValidator.ValidLoginConstraint;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class RegistRequest {

    @NotBlank(message = "login can't be empty")
    @ValidLoginConstraint
    private String login;

    @NotBlank(message = "password can't be empty")
    @Size(min = 5, max = 25, message = "length must be at least 5 symbols")
    private String password;

    @NotBlank(message = "name can't be empty")
    private String name;

    @NotBlank(message = "surname can't be empty")
    private String surname;

    @ValidMailConstraint
    private String email;

    public String getPassword() {
        return "{bcrypt}" + new BCryptPasswordEncoder().encode(password);
    }
}
