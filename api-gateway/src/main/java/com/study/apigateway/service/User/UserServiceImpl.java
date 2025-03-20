package com.study.apigateway.service.User;

import com.study.apigateway.dto.User.request.CreateUserRequest;
import com.study.apigateway.dto.User.request.UpdateUserRequest;
import com.study.apigateway.dto.User.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public UserResponse createUser(CreateUserRequest user) {
        return null;
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return null;
    }

    @Override
    public List<UserResponse> getUsersByListOfIds(List<UUID> ids, UUID cursor, int size) {
        return List.of();
    }

    @Override
    public List<UserResponse> searchUserByUsername(String keyword, UUID cursor, int size) {
        return List.of();
    }

    @Override
    public UserResponse updateUser(UUID id, UpdateUserRequest user, MultipartFile newAvatar) throws IOException {
        return null;
    }
}