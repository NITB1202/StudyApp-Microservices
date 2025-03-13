package com.study.userservice.services;

import com.study.userservice.dto.request.CreateUserRequest;
import com.study.userservice.dto.request.UpdateUserRequest;
import com.study.userservice.dto.response.UserResponse;
import com.study.userservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
//        if(userRepository.existsByUsername(request.getUsername()))
//            throw new ValidateException("Duplicate username");



        return null;
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return null;
    }

    @Override
    public UserResponse updateUser(UUID id, UpdateUserRequest user) {
        return null;
    }

    @Override
    public Page<UserResponse> searchUserByUsername(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public List<UserResponse> getUsersByListOfIds(List<UUID> ids) {
        return List.of();
    }
}
