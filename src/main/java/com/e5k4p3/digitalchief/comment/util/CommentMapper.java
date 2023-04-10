package com.e5k4p3.digitalchief.comment.util;

import com.e5k4p3.digitalchief.comment.dto.CommentRequestDto;
import com.e5k4p3.digitalchief.comment.model.Comment;
import com.e5k4p3.digitalchief.comment.dto.CommentResponseDto;
import com.e5k4p3.digitalchief.post.model.Post;
import com.e5k4p3.digitalchief.user.model.User;
import com.e5k4p3.digitalchief.user.utils.UserMapper;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public final class CommentMapper {
    public static Comment toComment(CommentRequestDto commentRequestDto, User author, Post post) {
        return new Comment(
                null,
                author,
                post,
                commentRequestDto.getText(),
                LocalDateTime.now(),
                false
        );
    }
    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getPost().getId(),
                UserMapper.toUserResponseShortDto(comment.getAuthor()),
                comment.getText(),
                comment.getCreated(),
                comment.getEdited()
        );
    }
}
