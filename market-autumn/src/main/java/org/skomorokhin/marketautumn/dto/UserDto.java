package org.skomorokhin.marketautumn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {


    private String login;

    private String name;

    private String surname;

    private String email;

}
