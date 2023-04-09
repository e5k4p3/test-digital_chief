package com.e5k4p3.digitalchief.user.controller;

import com.e5k4p3.digitalchief.user.dto.UserRequestDto;
import com.e5k4p3.digitalchief.user.dto.UserResponseFullDto;
import com.e5k4p3.digitalchief.user.service.UserService;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseFullDto createUser(@Validated @RequestBody UserRequestDto userRequestDto,
                                          BindingResult bindingResult) {
        ValidationErrorsHandler.logValidationErrors(bindingResult);
        return userService.createUser(userRequestDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseFullDto updateUser(@Validated @RequestBody UserRequestDto userRequestDto,
                                          BindingResult bindingResult,
                                          @PathVariable Long userId,
                                          @RequestHeader("X-Requester-Id") Long requesterId) {
        ValidationErrorsHandler.logValidationErrors(bindingResult);
        return userService.updateUser(userRequestDto, userId, requesterId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId,
                           @RequestHeader("X-Requester-Id") Long requesterId) {
        userService.deleteUser(userId, requesterId);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseFullDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseFullDto> getAllUsers(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.getAllUsers(from, size);
    }
}
