package com.study.userservice.service;

import com.study.userservice.enity.User;
import com.study.userservice.grpc.*;

import java.util.List;

public interface UserService {
    User createUser(CreateUserRequest request);
    User getUserById(GetUserByIdRequest request);
    List<User> searchUsersByUsername(SearchUserRequest request);
    long countUsersByUsername(String username);
    User updateUser(UpdateUserRequest request);
    void uploadUserAvatar(UploadUserAvatarRequest request);
    void validateUserId(ValidateUserIdRequest request);
}
