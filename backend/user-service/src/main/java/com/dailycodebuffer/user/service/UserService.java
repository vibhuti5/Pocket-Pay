package com.dailycodebuffer.user.service;

import com.dailycodebuffer.user.dto.UserDto;
import com.dailycodebuffer.user.dto.UserResponseDto;
import com.dailycodebuffer.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDto> getAllUsers();

    UserDto saveUser(UserDto userDto);

    UserDto getUserByEmail(String email);

    Optional<User> getByUserId(int id);
}
