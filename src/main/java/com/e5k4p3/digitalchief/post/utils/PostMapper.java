package com.e5k4p3.digitalchief.post.utils;

import com.e5k4p3.digitalchief.comment.util.CommentMapper;
import com.e5k4p3.digitalchief.post.dto.PostRequestDto;
import com.e5k4p3.digitalchief.post.dto.PostResponseDto;
import com.e5k4p3.digitalchief.post.model.Post;
import com.e5k4p3.digitalchief.user.model.User;
import com.e5k4p3.digitalchief.user.utils.UserMapper;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@UtilityClass
public final class PostMapper {
    public static Post toPost(PostRequestDto postRequestDto, User author) {
        return new Post(
                null,
                postRequestDto.getTitle(),
                postRequestDto.getContent(),
                author,
                new ArrayList<>(),
                LocalDateTime.now(),
                false,
                0L
        );
    }

    public static PostResponseDto toPostResponseDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                UserMapper.toUserResponseShortDto(post.getAuthor()),
                post.getComments().stream().map(CommentMapper::toCommentResponseDto).collect(Collectors.toList()),
                post.getCreated(),
                post.getEdited(),
                post.getViews()
        );
    }
}
