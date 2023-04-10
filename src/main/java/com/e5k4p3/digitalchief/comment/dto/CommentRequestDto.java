package com.e5k4p3.digitalchief.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequestDto {
    @NotNull(message = "Текст комментария не может быть null.")
    @NotBlank(message = "Текст комментария не может быть пустым.")
    String text;
}
