package com.e5k4p3.digitalchief.user.controller;

import com.e5k4p3.digitalchief.user.dto.UserRequestDto;
import com.e5k4p3.digitalchief.user.dto.UserResponseFullDto;
import com.e5k4p3.digitalchief.user.service.UserService;
import com.e5k4p3.digitalchief.utils.Create;
import com.e5k4p3.digitalchief.utils.Update;
import com.e5k4p3.digitalchief.utils.ValidationErrorsHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseFullDto createUser(@Validated(Create.class) @RequestBody UserRequestDto userRequestDto,
                                          BindingResult bindingResult) {
        log.debug("Получен POST запрос на добавление пользователя.");
        ValidationErrorsHandler.logValidationErrors(bindingResult);
        return userService.createUser(userRequestDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseFullDto updateUser(@Validated(Update.class) @RequestBody UserRequestDto userRequestDto,
                                          BindingResult bindingResult,
                                          @PathVariable Long userId,
                                          @RequestHeader("X-Requester-Id") Long requesterId) {
        log.debug("Получен PATCH запрос на изменение пользователя с id " + userId +
                " от пользователя с id " + requesterId + ".");
        ValidationErrorsHandler.logValidationErrors(bindingResult);
        return userService.updateUser(userRequestDto, userId, requesterId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId,
                           @RequestHeader("X-Requester-Id") Long requesterId) {
        log.debug("Получен DELETE запрос на удаление пользователя с id " + userId +
                " от пользователя с id " + requesterId + ".");
        userService.deleteUser(userId, requesterId);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseFullDto getUserById(@PathVariable Long userId) {
        log.debug("Получен GET запрос на получение пользователя с id " + userId + ".");
        return userService.getUserById(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseFullDto> getAllUsers(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.debug("Получен GET запрос на получение всех пользователей.");
        return userService.getAllUsers(from, size);
    }

    @DeleteMapping("/debug")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetData() {
        log.debug("Получен DELETE запрос на удаление всех данных о пользователях.");
        userService.resetData();
    }
}
