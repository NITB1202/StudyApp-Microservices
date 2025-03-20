package com.study.apigateway.service.User;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.study.apigateway.dto.User.request.CreateUserRequestDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.apigateway.dto.User.response.GetUsersByListOfIdsResponseDto;
import com.study.apigateway.dto.User.response.SearchUserResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.apigateway.grpcclient.UserServiceGrpcClient;
import com.study.apigateway.mapper.UserMapper;
import com.study.common.exceptions.BusinessException;
import com.study.userservice.grpc.GetUsersByListOfIdsResponse;
import com.study.userservice.grpc.SearchUserResponse;
import com.study.userservice.grpc.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserServiceGrpcClient userServiceGrpcClient;
    private final Cloudinary cloudinary;

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
        //Check if the client upload an image file
        if(newAvatar != null && !newAvatar.isEmpty()){
            String fileType = newAvatar.getContentType();
            if(fileType == null || !fileType.startsWith("image/"))
                throw new BusinessException("New avatar is not an image");
            else {
                //Upload the new avatar to the Cloudinary
                Map params = ObjectUtils.asMap(
                        "resource_type", "auto",
                        "asset_folder", "avatars",
                        "public_id", id.toString(),
                        "overwrite", true
                );

                Map result = cloudinary.uploader().upload(newAvatar.getBytes(),params);

                String newAvatarUrl = result.get("secure_url").toString();
                request.setAvatarUrl(newAvatarUrl);
            }
        }

        UserResponse user = userServiceGrpcClient.updateUser(id, request);
        return UserMapper.toResponseDto(user);
    }
}