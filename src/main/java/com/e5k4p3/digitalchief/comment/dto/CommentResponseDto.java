package com.e5k4p3.digitalchief.comment.dto;

import com.e5k4p3.digitalchief.user.dto.UserResponseShortDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponseDto {
    Long id;
    Long postId;
    UserResponseShortDto author;
    String text;
    LocalDateTime created;
    Boolean edited;
}
