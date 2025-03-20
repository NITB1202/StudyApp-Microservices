package com.study.apigateway.service.User;

import com.study.apigateway.dto.User.request.CreateUserRequestDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.apigateway.dto.User.response.GetUsersByListOfIdsResponseDto;
import com.study.apigateway.dto.User.response.SearchUserResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.apigateway.grpcclient.UserServiceGrpcClient;
import com.study.apigateway.mapper.UserMapper;
import com.study.userservice.grpc.GetUsersByListOfIdsResponse;
import com.study.userservice.grpc.SearchUserResponse;
import com.study.userservice.grpc.UserResponse;
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
        UserResponse user = userServiceGrpcClient.createUser(request);
        return UserMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
        UserResponse user = userServiceGrpcClient.getUserById(id);
        return UserMapper.toResponseDto(user);
    }

    @Override
    public GetUsersByListOfIdsResponseDto getUsersByListOfIds(List<UUID> ids, UUID cursor, int size) {
        GetUsersByListOfIdsResponse response = userServiceGrpcClient.getUsersByListOfIds(ids, cursor, size);
        UUID nextCursor = response.getNextCursor().isEmpty() ? null : UUID.fromString(response.getNextCursor());
        List<UserResponseDto> users = UserMapper.toResponseDtoList(response.getUsersList());

        return GetUsersByListOfIdsResponseDto.builder()
                .users(users)
                .total(response.getTotal())
                .size(response.getSize())
                .nextCursor(nextCursor)
                .build();
    }

    @Override
    public SearchUserResponseDto searchUserByUsername(String keyword, UUID cursor, int size) {
        SearchUserResponse response = userServiceGrpcClient.searchUserByUsername(keyword, cursor, size);
        UUID nextCursor = response.getNextCursor().isEmpty() ? null : UUID.fromString(response.getNextCursor());
        List<UserResponseDto> users = UserMapper.toResponseDtoList(response.getUsersList());

        return SearchUserResponseDto.builder()
                .users(users)
                .total(response.getTotal())
                .nextCursor(nextCursor)
                .build();
    }

    @Override
    public UserResponseDto updateUser(UUID id, UpdateUserRequestDto request, MultipartFile newAvatar) throws IOException {
        UserResponse user = userServiceGrpcClient.updateUser(id, request);
        return UserMapper.toResponseDto(user);
    }
}