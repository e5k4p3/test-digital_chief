package com.e5k4p3.digitalchief.post.dto;

import com.e5k4p3.digitalchief.utils.Create;
import com.e5k4p3.digitalchief.utils.Update;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequestDto {
    @NotNull(message = "Название поста не может быть null.", groups = Create.class)
    @Size(min = 10, max = 200, message = "Кол-во символов в название поста дожно быть в диапазоне от 10 до 200 символов",
            groups = {Create.class, Update.class})
    String title;
    @NotNull(message = "Содержание поста не может быть null.", groups = Create.class)
    @Size(min = 20, max = 10000, message = "Кол-во символов в содержании поста должно быть в диапазоне от 20 до 10000 символов",
            groups = {Create.class, Update.class})
    String content;
}
