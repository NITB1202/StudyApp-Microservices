package com.study.apigateway.service.User;

import com.study.apigateway.dto.User.request.CreateUserRequestDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.apigateway.dto.User.response.ListUserResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.apigateway.grpcclient.UserServiceGrpcClient;
import com.study.apigateway.mapper.UserMapper;
import com.study.apigateway.utils.AvatarUtils;
import com.study.userservice.grpc.ListUserResponse;
import com.study.userservice.grpc.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserServiceGrpcClient userServiceGrpcClient;
    private final AvatarUtils avatarUtils;

    @Override
    public Mono<UserResponseDto> createUser(CreateUserRequestDto request) {
        return Mono.fromCallable(() -> {
            UserResponse user = userServiceGrpcClient.createUser(request);
            return UserMapper.toResponseDto(user);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UserResponseDto> getUserById(UUID id) {
        return Mono.fromCallable(() -> {
            UserResponse user = userServiceGrpcClient.getUserById(id);
            return UserMapper.toResponseDto(user);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public ListUserResponseDto getUsersByListOfIds(List<UUID> ids, UUID cursor, int size) {
        ListUserResponse response = userServiceGrpcClient.getUsersByListOfIds(ids, cursor, size);

        UUID nextCursor = response.getNextCursor().isEmpty() ? null : UUID.fromString(response.getNextCursor());
        List<UserResponseDto> users = UserMapper.toResponseDtoList(response.getUsersList());

        return ListUserResponseDto.builder()
                .users(users)
                .total(response.getTotal())
                .nextCursor(nextCursor)
                .build();
    }

    @Override
    public Mono<ListUserResponseDto> searchUserByUsername(String keyword, UUID cursor, int size) {
        return Mono.fromCallable(() -> {
            ListUserResponse response = userServiceGrpcClient.searchUserByUsername(keyword, cursor, size);

            UUID nextCursor = response.getNextCursor().isEmpty() ? null : UUID.fromString(response.getNextCursor());
            List<UserResponseDto> users = UserMapper.toResponseDtoList(response.getUsersList());

            return ListUserResponseDto.builder()
                    .users(users)
                    .total(response.getTotal())
                    .nextCursor(nextCursor)
                    .build();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UserResponseDto> updateUser(UUID id, UpdateUserRequestDto request, FilePart newAvatar){
        return Mono.fromCallable(() -> {
            //In case the request is null
            UpdateUserRequestDto handledRequest = request != null ? request : new UpdateUserRequestDto();

            if (newAvatar != null) {
                String newAvatarUrl = avatarUtils.uploadAvatar(id, newAvatar);
                handledRequest.setAvatarUrl(newAvatarUrl);
            }

            UserResponse user = userServiceGrpcClient.updateUser(id, handledRequest);

            return UserMapper.toResponseDto(user);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}