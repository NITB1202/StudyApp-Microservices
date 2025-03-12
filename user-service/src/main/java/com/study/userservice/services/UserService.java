package com.study.userservice.services;

import com.study.userservice.dtos.CreateUserRequest;
import com.study.userservice.dtos.UpdateUserRequest;
import com.study.userservice.dtos.UserResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {
    UserResponse createUser(CreateUserRequest user);
    UserResponse getUserById(UUID id);
    UserResponse updateUser(UpdateUserRequest user);
}
