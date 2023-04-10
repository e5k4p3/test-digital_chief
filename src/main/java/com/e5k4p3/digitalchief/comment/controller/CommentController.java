package com.e5k4p3.digitalchief.comment.controller;

import com.e5k4p3.digitalchief.comment.dto.CommentRequestDto;
import com.e5k4p3.digitalchief.comment.dto.CommentResponseDto;
import com.e5k4p3.digitalchief.comment.service.CommentService;
import com.e5k4p3.digitalchief.utils.ValidationErrorsHandler;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentController {
    final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
                                            BindingResult bindingResult,
                                            @RequestHeader("X-Requester-Id") Long requesterId,
                                            @PathVariable Long postId) {
        log.debug("Получен POST запрос на добавдение комментария от пользователя с id "
                + requesterId + " к посту с id " + postId + ".");
        ValidationErrorsHandler.logValidationErrors(bindingResult);
        return commentService.createComment(commentRequestDto, requesterId, postId);
    }

    @PatchMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto updateComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
                                            BindingResult bindingResult,
                                            @RequestHeader("X-Requester-Id") Long requesterId,
                                            @PathVariable Long commentId) {
        log.debug("Получен PATCH запрос от пользователя с id " + requesterId +
                " на изменение комметария с id " + commentId + ".");
        ValidationErrorsHandler.logValidationErrors(bindingResult);
        return commentService.updateComment(commentRequestDto, commentId, requesterId);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId,
                              @RequestHeader("X-Requester-Id") Long requesterId) {
        log.debug("Получен DELETE запрос от пользователя с id " + requesterId +
                " на удаление комментария с id " + commentId + ".");
        commentService.deleteComment(commentId, requesterId);
    }

    @GetMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto getCommentById(@PathVariable Long commentId) {
        log.debug("Получен GET запрос на получение комментария с id " + commentId + ".");
        return commentService.getCommentById(commentId);
    }

    @GetMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getAllCommentsByPostId(@PathVariable Long postId) {
        log.debug("Получен GET запрос на получение всех комментариев к посту с id " + postId + ".");
        return commentService.getAllCommentsByPostId(postId);
    }

    @DeleteMapping("/comments/debug")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetData() {
        log.debug("Получен DELETE запрос на удаление всех комментариев.");
        commentService.resetData();
    }
}
