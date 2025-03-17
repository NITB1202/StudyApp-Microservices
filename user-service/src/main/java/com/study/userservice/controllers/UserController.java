package com.study.userservice.controllers;

import com.study.userservice.dto.request.CreateUserRequest;
import com.study.userservice.dto.request.UpdateUserRequest;
import com.study.userservice.dto.response.UserResponse;
import com.study.userservice.exceptions.ErrorResponse;
import com.study.userservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    @Operation(summary = "Create a new user.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user information by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/list")
    @Operation(summary = "Get a list of users by a list of ids.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public ResponseEntity<List<UserResponse>> getUsersByListOfIdS(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(userService.getUsersByListOfIds(ids));
    }

    @GetMapping("/search")
    @Operation(summary = "Search for users by username")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    public ResponseEntity<Page<UserResponse>> searchUsersByUsername(@RequestParam String keyword,
                                                                    @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(userService.searchUserByUsername(keyword, pageable));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update a specific user.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id,
                                                   @Valid @RequestPart(value = "request", required = false) UpdateUserRequest request,
                                                   @RequestPart(value = "file", required = false) MultipartFile newAvatar) throws IOException {
        return ResponseEntity.ok(userService.updateUser(id, request, newAvatar));
    }
}
