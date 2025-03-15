package com.study.userservice.services;

import com.study.userservice.dto.request.CreateUserRequest;
import com.study.userservice.dto.request.UpdateUserRequest;
import com.study.userservice.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(CreateUserRequest user);
    UserResponse getUserById(UUID id);
    List<UserResponse> getUsersByListOfIds(List<UUID> ids);
    Page<UserResponse> searchUserByUsername(String keyword, Pageable pageable);
    UserResponse updateUser(UUID id, UpdateUserRequest user, MultipartFile newAvatar) throws IOException;
}