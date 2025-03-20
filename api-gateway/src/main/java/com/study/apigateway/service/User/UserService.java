package com.study.apigateway.service.User;

import com.study.apigateway.dto.User.request.CreateUserRequestDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.apigateway.dto.User.response.GetUsersByListOfIdsResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(CreateUserRequestDto user);
    UserResponseDto getUserById(UUID id);
    GetUsersByListOfIdsResponseDto getUsersByListOfIds(List<UUID> ids, UUID cursor, int size);
    List<UserResponseDto> searchUserByUsername(String keyword, UUID cursor, int size);
    UserResponseDto updateUser(UUID id, UpdateUserRequestDto user, MultipartFile newAvatar) throws IOException;
}
