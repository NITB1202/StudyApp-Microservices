package com.study.apigateway.service.User;

import com.study.apigateway.dto.User.request.CreateUserRequest;
import com.study.apigateway.dto.User.request.UpdateUserRequest;
import com.study.apigateway.dto.User.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(CreateUserRequest user);
    UserResponse getUserById(UUID id);
    List<UserResponse> getUsersByListOfIds(List<UUID> ids, UUID cursor, int size);
    List<UserResponse> searchUserByUsername(String keyword, UUID cursor, int size);
    UserResponse updateUser(UUID id, UpdateUserRequest user, MultipartFile newAvatar) throws IOException;
}
