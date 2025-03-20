package com.study.apigateway.controller;

import com.study.apigateway.dto.User.request.CreateUserRequest;
import com.study.apigateway.dto.User.request.UpdateUserRequest;
import com.study.apigateway.dto.User.response.UserResponse;
import com.study.apigateway.service.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/list")
    public ResponseEntity<List<UserResponse>> getUsersByListOfIds(@Valid @RequestBody List<UUID> ids,
                                                                  @RequestParam(required = false) UUID cursor,
                                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsersByListOfIds(ids, cursor, size));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsersByUsername(@RequestParam String keyword,
                                                                    @RequestParam(required = false) UUID cursor,
                                                                    @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.searchUserByUsername(keyword, cursor, size));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id,
                                                   @Valid @RequestPart(value = "request", required = false) UpdateUserRequest request,
                                                   @RequestPart(value = "file", required = false) MultipartFile newAvatar) throws IOException {
        return ResponseEntity.ok(userService.updateUser(id, request, newAvatar));
    }
}
