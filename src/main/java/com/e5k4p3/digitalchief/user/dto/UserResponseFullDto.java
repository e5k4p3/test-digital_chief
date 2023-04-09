package com.e5k4p3.digitalchief.user.dto;

import com.e5k4p3.digitalchief.user.model.enums.UserGender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseFullDto {
    Long id;
    String name;
    String surname;
    String email;
    Integer age;
    UserGender gender;
}
