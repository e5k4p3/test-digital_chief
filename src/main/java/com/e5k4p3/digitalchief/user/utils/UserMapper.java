package com.e5k4p3.digitalchief.user.utils;

import com.e5k4p3.digitalchief.user.dto.UserRequestDto;
import com.e5k4p3.digitalchief.user.dto.UserResponseFullDto;
import com.e5k4p3.digitalchief.user.dto.UserResponseShortDto;
import com.e5k4p3.digitalchief.user.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserMapper {
    public static User toUser(UserRequestDto userRequestDto) {
        return new User(
                null,
                userRequestDto.getName(),
                userRequestDto.getSurname(),
                userRequestDto.getEmail(),
                userRequestDto.getAge(),
                userRequestDto.getGender()
        );
    }

    public static UserResponseFullDto toUserResponseFullDto(User user) {
        return new UserResponseFullDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getAge(),
                user.getGender()
        );
    }

    public static UserResponseShortDto toUserResponseShortDto(User user) {
        return new UserResponseShortDto(
                user.getName(),
                user.getSurname()
        );
    }
}
