package com.study.apigateway.service.User;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.apigateway.dto.User.response.ListUserResponseDto;
import com.study.apigateway.dto.User.response.UserDetailResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<UserDetailResponseDto> getUserById(UUID id);
    Mono<ListUserResponseDto> searchUserByUsername(String keyword, UUID cursor, int size);
    Mono<UserResponseDto> updateUser(UUID id, UpdateUserRequestDto user);
    Mono<ActionResponseDto> uploadUserAvatar(UUID id, FilePart file);
}
