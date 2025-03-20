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
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/list")
    public ResponseEntity<GetUsersByListOfIdsResponseDto> getUsersByListOfIds(@Valid @RequestBody List<UUID> ids,
                                                                              @RequestParam(required = false) UUID cursor,
                                                                              @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getUsersByListOfIds(ids, cursor, size));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchUserResponseDto> searchUsersByUsername(@RequestParam String keyword,
                                                                       @RequestParam(required = false) UUID cursor,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.searchUserByUsername(keyword, cursor, size));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id,
                                                      @Valid @RequestPart(value = "request", required = false) UpdateUserRequestDto request,
                                                      @RequestPart(value = "file", required = false) MultipartFile newAvatar) throws IOException {
        return ResponseEntity.ok(userService.updateUser(id, request, newAvatar));
    }
}
