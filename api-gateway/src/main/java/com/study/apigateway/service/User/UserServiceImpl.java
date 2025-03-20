package com.study.apigateway.service.User;

import com.study.apigateway.dto.User.request.CreateUserRequestDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.apigateway.dto.User.response.GetUsersByListOfIdsResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.apigateway.grpcclient.UserServiceGrpcClient;
import com.study.apigateway.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserServiceGrpcClient userServiceGrpcClient;

    @Override
    public UserResponseDto createUser(CreateUserRequestDto request) {
        var user = userServiceGrpcClient.createUser(request);
        return UserMapper.responseToResponseDto(user);
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
        var user = userServiceGrpcClient.getUserById(id);
        return UserMapper.responseToResponseDto(user);
    }

    @Override
    public GetUsersByListOfIdsResponseDto getUsersByListOfIds(List<UUID> ids, UUID cursor, int size) {
        var response = userServiceGrpcClient.getUsersByListOfIds(ids, cursor, size);

    }

    @Override
    public List<UserResponseDto> searchUserByUsername(String keyword, UUID cursor, int size) {
        return List.of();
    }

    @Override
    public UserResponseDto updateUser(UUID id, UpdateUserRequestDto user, MultipartFile newAvatar) throws IOException {
        return null;
    }
}