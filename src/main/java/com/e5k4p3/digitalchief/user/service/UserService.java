package com.e5k4p3.digitalchief.user.service;

import com.e5k4p3.digitalchief.user.dto.UserRequestDto;
import com.e5k4p3.digitalchief.user.dto.UserResponseFullDto;

import java.util.List;

public interface UserService {
    UserResponseFullDto createUser(UserRequestDto userRequestDto);

    UserResponseFullDto updateUser(UserRequestDto userRequestDto, Long userId, Long requesterId);

    void deleteUser(Long userId, Long requesterId);

    UserResponseFullDto getUserById(Long userId);

    List<UserResponseFullDto> getAllUsers(Integer from, Integer size);

    void resetData();
}
