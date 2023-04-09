package com.e5k4p3.digitalchief.post.dto;

import com.e5k4p3.digitalchief.comment.CommentResponseDto;
import com.e5k4p3.digitalchief.user.dto.UserResponseShortDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponseDto {
    Long id;
    String title;
    String content;
    UserResponseShortDto author;
    List<CommentResponseDto> comments;
    LocalDateTime created;
    Boolean edited;
    Long views;
}
