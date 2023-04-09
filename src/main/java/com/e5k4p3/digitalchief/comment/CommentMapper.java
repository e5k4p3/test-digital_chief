package com.e5k4p3.digitalchief.comment;

import com.e5k4p3.digitalchief.user.utils.UserMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CommentMapper {
    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                UserMapper.toUserResponseShortDto(comment.getAuthor()),
                comment.getText(),
                comment.getCreated(),
                comment.getEdited()
        );
    }
}
