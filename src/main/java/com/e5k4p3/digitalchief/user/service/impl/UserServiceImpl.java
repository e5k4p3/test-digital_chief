package com.e5k4p3.digitalchief.user.service.impl;

import com.e5k4p3.digitalchief.exceptions.EntityAlreadyExistsException;
import com.e5k4p3.digitalchief.exceptions.EntityNotFoundException;
import com.e5k4p3.digitalchief.exceptions.ForbiddenOperationException;
import com.e5k4p3.digitalchief.user.dao.UserRepository;
import com.e5k4p3.digitalchief.user.dto.UserRequestDto;
import com.e5k4p3.digitalchief.user.dto.UserResponseFullDto;
import com.e5k4p3.digitalchief.user.model.User;
import com.e5k4p3.digitalchief.user.model.enums.UserGender;
import com.e5k4p3.digitalchief.user.service.UserService;
import com.e5k4p3.digitalchief.user.utils.UserMapper;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final EntityManager entityManager;

    @Override
    @Transactional
    public UserResponseFullDto createUser(UserRequestDto userRequestDto) {
        checkEmailExistence(userRequestDto.getEmail());
        User userToAdd = userRepository.save(UserMapper.toUser(userRequestDto));
        log.info("Пользователь с email " + userToAdd.getEmail() + " был создан под id " + userToAdd.getId() + ".");
        return UserMapper.toUserResponseFullDto(userToAdd);
    }

    @Override
    @Transactional
    public UserResponseFullDto updateUser(UserRequestDto userRequestDto, Long userId, Long requesterId) {
        checkUserIdAndRequesterId(userId, requesterId);
        User userToUpdate = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Пользователь с id " + userId + " не найден."));
        String commonPhrase = "У пользователя с id " + userId;
        if (userRequestDto.getName() != null && !userRequestDto.getName().isBlank()) {
            String oldName = userToUpdate.getName();
            String newName = userRequestDto.getName();
            userToUpdate.setName(newName);
            log.info(commonPhrase + " было изменено имя с " + oldName + " на " + newName + ".");
        }
        if (userRequestDto.getSurname() != null && !userRequestDto.getSurname().isBlank()) {
            String oldSurname = userToUpdate.getSurname();
            String newSurname = userRequestDto.getSurname();
            userToUpdate.setSurname(newSurname);
            log.info(commonPhrase + " была изменена фамилия с " + oldSurname + " на " + newSurname + ".");
        }
        if (userRequestDto.getEmail() != null && !userRequestDto.getEmail().isBlank()) {
            String oldEmail = userToUpdate.getEmail();
            String newEmail = userRequestDto.getEmail();
            checkEmailExistence(newEmail);
            userToUpdate.setEmail(newEmail);
            log.info(commonPhrase + " был изменен email с " + oldEmail + " на " + newEmail + ".");
        }
        if (userRequestDto.getGender() != null) {
            UserGender oldGender = userToUpdate.getGender();
            UserGender newGender = userRequestDto.getGender();
            userToUpdate.setGender(newGender);
            log.info(commonPhrase + " был изменен пол с " + oldGender + " на " + newGender + ".");
        }
        return UserMapper.toUserResponseFullDto(userToUpdate);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId, Long requesterId) {
        checkUserIdAndRequesterId(userId, requesterId);
        userRepository.deleteById(userId);
        log.info("Пользователь с id " + userId + " был удален.");
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseFullDto getUserById(Long userId) {
        log.info("Возвращаем данные о пользователе с id " + userId + ".");
        return UserMapper.toUserResponseFullDto(userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Пользователь с id " + userId + " не найден.")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseFullDto> getAllUsers(Integer from, Integer size) {
        log.info("Возвращаем данные всех пользователей.");
        return userRepository.findAll(PageRequest.of((from / size), size)).stream()
                .map(UserMapper::toUserResponseFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void resetData() {
        entityManager.createNativeQuery("TRUNCATE users RESTART IDENTITY").executeUpdate();
        log.info("Данные обо всех пользователях удалены.");
    }

    private void checkEmailExistence(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsException("Пользователь с email " + email + " уже существует.");
        }
    }

    private Long checkUserExistenceById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь с id " + userId + " не найден.");
        }
        return userId;
    }

    private void checkUserIdAndRequesterId(Long userId, Long requesterId) {
        if (!checkUserExistenceById(userId).equals(checkUserExistenceById(requesterId))) {
            throw new ForbiddenOperationException("Пользователь с id " + requesterId +
                    " не может изменить данные пользователя с id " + userId + ".");
        }
    }
}
