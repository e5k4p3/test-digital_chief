package com.e5k4p3.digitalchief.exceptions.handler;

import com.e5k4p3.digitalchief.comment.controller.CommentController;
import com.e5k4p3.digitalchief.exceptions.EntityAlreadyExistsException;
import com.e5k4p3.digitalchief.exceptions.EntityNotFoundException;
import com.e5k4p3.digitalchief.exceptions.ForbiddenOperationException;
import com.e5k4p3.digitalchief.exceptions.ValidationException;
import com.e5k4p3.digitalchief.post.controller.PostController;
import com.e5k4p3.digitalchief.user.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {UserController.class, PostController.class, CommentController.class})
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final ValidationException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "Ошибка валидации",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final EntityNotFoundException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.toString(),
                "Объект не найден.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExists(final EntityAlreadyExistsException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.toString(),
                "Объект уже существует.",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(final ForbiddenOperationException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                HttpStatus.FORBIDDEN.toString(),
                "Отсутствие прав доступа.",
                e.getMessage()
        );
    }
}
