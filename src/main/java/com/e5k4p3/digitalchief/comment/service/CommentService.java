package com.e5k4p3.digitalchief.comment.service;

import com.e5k4p3.digitalchief.comment.dto.CommentRequestDto;
import com.e5k4p3.digitalchief.comment.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long requesterId, Long postId);

    CommentResponseDto updateComment(CommentRequestDto commentRequestDto, Long commentId, Long requesterId);

    void deleteComment(Long commentId, Long requesterId);

    CommentResponseDto getCommentById(Long commentId);

    List<CommentResponseDto> getAllCommentsByPostId(Long postId);

    void resetData();
}
