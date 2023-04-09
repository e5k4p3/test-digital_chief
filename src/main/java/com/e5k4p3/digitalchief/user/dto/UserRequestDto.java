package com.e5k4p3.digitalchief.user.dto;

import com.e5k4p3.digitalchief.user.model.enums.UserGender;
import com.e5k4p3.digitalchief.utils.Create;
import com.e5k4p3.digitalchief.utils.Update;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDto {
    @NotNull(message = "Имя пользователя не может быть null.", groups = Create.class)
    @NotBlank(message = "Имя пользователя не может быть пустым.", groups = {Create.class, Update.class})
    @Size(min = 2, max = 30, message = "Кол-во символов в имени должно быть в диапазоне от 2 до 30.", groups = {Create.class, Update.class})
    String name;
    @NotNull(message = "Фамилия пользователя не может быть null.", groups = Create.class)
    @NotBlank(message = "Фамилия пользователя не может быть пустой.", groups = {Create.class, Update.class})
    @Size(min = 2, max = 50, message = "Кол-во символов в фамилии должно быть в диапазоне от 2 до 50.", groups = {Create.class, Update.class})
    String surname;
    @NotNull(message = "Email не может быть null.", groups = Create.class)
    @Email(message = "Email должен быть корректным.", groups = {Create.class, Update.class})
    String email;
    @Positive(message = "Возраст пользователя не может быть отрицательным.", groups = {Create.class, Update.class})
    @Max(value = 120, message = "Возраст пользователя не может превышать 120 лет.", groups = {Create.class, Update.class})
    Integer age;
    @NotNull(message = "Пол пользователя не может быть null.", groups = Create.class)
    UserGender gender;
}
