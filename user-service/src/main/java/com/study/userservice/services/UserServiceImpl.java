package com.study.userservice.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.study.userservice.dto.request.CreateUserRequest;
import com.study.userservice.models.User;
import com.study.userservice.dto.request.UpdateUserRequest;
import com.study.userservice.dto.response.UserResponse;
import com.study.userservice.exceptions.BusinessException;
import com.study.userservice.exceptions.NotFoundException;
import com.study.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;

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
    public UserResponse updateUser(UUID id, UpdateUserRequest request, MultipartFile newAvatar) throws IOException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with id " + id + " not found")
        );

        //Check if username has already existed, ignore the case username is unchanged.
        if(request.getUsername() != null &&
               !request.getUsername().equals(user.getUsername()) &&
               userRepository.existsByUsername(request.getUsername()))
           throw new BusinessException("Username already exists");

       modelMapper.map(request, user);

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
                user.setAvatarUrl(newAvatarUrl);
            }
        }

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
