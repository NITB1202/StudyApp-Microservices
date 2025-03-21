package com.study.apigateway.controller;

import com.study.apigateway.dto.User.request.CreateUserRequestDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.apigateway.dto.User.response.GetUsersByListOfIdsResponseDto;
import com.study.apigateway.dto.User.response.SearchUserResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.apigateway.service.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public Mono<ResponseEntity<UserResponseDto>> createUser(@Valid @RequestBody CreateUserRequestDto request) {
        return userService.createUser(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponseDto>> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/list")
    public Mono<ResponseEntity<GetUsersByListOfIdsResponseDto>> getUsersByListOfIds(@Valid @RequestBody List<UUID> ids,
                                                                                    @RequestParam(required = false) UUID cursor,
                                                                                    @RequestParam(defaultValue = "10") int size) {
        return userService.getUsersByListOfIds(ids, cursor, size)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<SearchUserResponseDto>> searchUsersByUsername(@RequestParam String keyword,
                                                                             @RequestParam(required = false) UUID cursor,
                                                                             @RequestParam(defaultValue = "10") int size) {
        return userService.searchUserByUsername(keyword, cursor, size)
                .map(ResponseEntity::ok);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<UserResponseDto>> updateUser(
            @PathVariable UUID id,
            @RequestPart(value = "request", required = false) UpdateUserRequestDto request,
            @RequestPart(value = "file", required = false) FilePart newAvatar) {

        return userService.updateUser(id, request, newAvatar)
                .map(ResponseEntity::ok);
    }
}
