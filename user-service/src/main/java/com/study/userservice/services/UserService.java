package com.study.userservice.services;

import com.study.userservice.dto.CreateUserRequest;
import com.study.userservice.dto.UpdateUserRequest;
import com.study.userservice.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {
    UserResponse createUser(CreateUserRequest user);
    UserResponse getUserById(UUID id);
    UserResponse updateUser(UUID id, UpdateUserRequest user);
}
