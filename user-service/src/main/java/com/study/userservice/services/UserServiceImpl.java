package com.study.userservice.services;

import com.study.userservice.models.User;
import com.study.userservice.dto.request.CreateUserRequest;
import com.study.userservice.dto.request.UpdateUserRequest;
import com.study.userservice.dto.response.UserResponse;
import com.study.userservice.exceptions.BusinessException;
import com.study.userservice.exceptions.NotFoundException;
import com.study.userservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if(userRepository.existsByUsername(request.getUsername()))
            throw new BusinessException("Username already exists");

        User user = User.builder()
                .username(request.getUsername())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .build();

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserResponse.class);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with id " + id + " not found")
        );

        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with id " + id + " not found")
        );

        //Check if username has already existed, ignore the case username is unchanged.
        if(request.getUsername() != null &&
               !request.getUsername().equals(user.getUsername()) &&
               userRepository.existsByUsername(request.getUsername()))
           throw new BusinessException("Username already exists");

       modelMapper.map(request, user);

       userRepository.save(user);

       return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public Page<UserResponse> searchUserByUsername(String keyword, Pageable pageable) {
        Page<User> users = userRepository.findByUsernameContaining(keyword, pageable);
        return users.map(user -> modelMapper.map(user, UserResponse.class));
    }

    @Override
    public List<UserResponse> getUsersByListOfIds(List<UUID> ids) {
        List<User> users = userRepository.findAllById(ids);
        return modelMapper.map(users, new TypeToken<List<UserResponse>>() {}. getType());
    }
}
