package com.study.userservice.service;

import com.study.common.enums.Gender;
import com.study.common.exceptions.BusinessException;
import com.study.common.exceptions.NotFoundException;
import com.study.userservice.enity.User;
import com.study.userservice.grpc.*;
import com.study.userservice.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcServiceImpl extends UserServiceGrpc.UserServiceImplBase{
    private final UserRepository userRepository;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver){
        if(userRepository.existsByUsernameIgnoreCase(request.getUsername()))
            throw new BusinessException("Username already exists");

        User user = User.builder()
                .username(request.getUsername())
                .dateOfBirth(LocalDate.parse(request.getDateOfBirth()))
                .gender(protoEnumToEnum(request.getGender()))
                .build();

        userRepository.save(user);

        UserResponse response = buildUserResponse(user);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<UserResponse> responseObserver){
        User user = userRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        UserResponse response = buildUserResponse(user);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUsersByListOfIds(GetUsersByListOfIdsRequest request, StreamObserver<GetUsersByListOfIdsResponse> responseObserver){
        // Parse list of IDs
        List<UUID> ids = request.getIdsList().stream()
                .map(UUID::fromString)
                .toList();

        // Parse cursor (nullable)
        UUID cursor = request.getCursor().isBlank() ? null : UUID.fromString(request.getCursor());

        int size = request.getSize() > 0 ? request.getSize() : 10;
        Pageable pageable = PageRequest.of(0, size, Sort.by("id").ascending());

        List<User> users = userRepository.findByIdsWithCursor(ids, cursor, pageable);

        List<UserResponse> userResponses = users.stream()
                .map(this::buildUserResponse)
                .toList();

        // Determine next cursor
        String nextCursor = !users.isEmpty() && users.size() == size ? users.get(users.size() - 1).getId().toString() : "";

        GetUsersByListOfIdsResponse response = GetUsersByListOfIdsResponse.newBuilder()
                .addAllUsers(userResponses)
                .setTotal(userResponses.size())
                .setSize(size)
                .setNextCursor(nextCursor)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchUserByUsername(SearchUserRequest request, StreamObserver<SearchUserResponse> responseObserver){
        int size = request.getSize() > 0 ? request.getSize() : 10;

        //If the cursor is null or empty then fetch from the start
        UUID cursor = request.getCursor().isEmpty() ? null : UUID.fromString(request.getCursor());

        Pageable pageable = PageRequest.of(0, size, Sort.by("id").ascending());
        List<User> users = userRepository.searchByUsernameWithCursor(request.getKeyword(), cursor, pageable);

        List<UserResponse> userResponses = users.stream()
                .map(this::buildUserResponse)
                .toList();

        //Determine next cursor
        String nextCursor = users.isEmpty() ? "" : users.get(users.size() - 1).getId().toString();

        SearchUserResponse response = SearchUserResponse.newBuilder()
                .addAllUsers(userResponses)
                .setNextCursor(nextCursor)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<UserResponse> responseObserver){
        User user = userRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        //Check if username has already existed, ignore the case username is unchanged.
        if(!request.getUsername().isBlank()){
            if(!request.getUsername().equals(user.getUsername()) &&
                    userRepository.existsByUsernameIgnoreCase(request.getUsername()))
                throw new BusinessException("Username already exists");
            else
                user.setUsername(request.getUsername());
        }

        if(!request.getDateOfBirth().isBlank())
            user.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));

        if(request.getGender() != com.study.userservice.grpc.Gender.UNSPECIFIED)
            user.setGender(protoEnumToEnum(request.getGender()));

        if(!request.getAvatarUrl().isBlank())
            user.setAvatarUrl(request.getAvatarUrl());

        userRepository.save(user);

        UserResponse response = buildUserResponse(user);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private Gender protoEnumToEnum(com.study.userservice.grpc.Gender protoGenderEnum){
        return switch (protoGenderEnum) {
            case MALE -> Gender.MALE;
            case FEMALE -> Gender.FEMALE;
            default -> Gender.OTHER;
        };
    }

    private com.study.userservice.grpc.Gender enumToProtoEnum(Gender genderEnum){
        return switch(genderEnum) {
            case MALE -> com.study.userservice.grpc.Gender.MALE;
            case FEMALE -> com.study.userservice.grpc.Gender.FEMALE;
            default -> com.study.userservice.grpc.Gender.OTHER;
        };
    }

    private UserResponse buildUserResponse(User user){
        return UserResponse.newBuilder()
                .setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setDateOfBirth(user.getDateOfBirth().toString())
                .setGender(enumToProtoEnum(user.getGender()))
                .setAvatarUrl(Objects.requireNonNullElse(user.getAvatarUrl(), ""))
                .build();
    }
}
