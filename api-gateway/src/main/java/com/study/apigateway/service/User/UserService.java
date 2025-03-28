package com.study.apigateway.service.User;

import com.study.apigateway.dto.User.request.CreateUserRequestDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.apigateway.dto.User.response.ListUserResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface UserService {
    Mono<UserResponseDto> createUser(CreateUserRequestDto user);
    Mono<UserResponseDto> getUserById(UUID id);
    Mono<ListUserResponseDto> searchUserByUsername(String keyword, UUID cursor, int size);
    Mono<UserResponseDto> updateUser(UUID id, UpdateUserRequestDto user, FilePart newAvatar);

    //internal -> Used by another service
    ListUserResponseDto getUsersByListOfIds(List<UUID> ids, UUID cursor, int size);
}
