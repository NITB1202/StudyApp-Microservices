package com.study.userservice.controllers;

import com.study.userservice.dto.request.CreateUserRequest;
import com.study.userservice.dto.request.UpdateUserRequest;
import com.study.userservice.dto.response.UserResponse;
import com.study.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<UserResponse>> getUsersByListOfIdS(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(userService.getUsersByListOfIds(ids));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponse>> searchUsersByUsername(@RequestParam String keyword,
                                                                    @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(userService.searchUserByUsername(keyword, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id,
                                                   @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }
}
