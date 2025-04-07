package com.study.apigateway.controller;

import com.study.apigateway.dto.User.request.CreateUserRequestDto;
import com.study.apigateway.dto.User.request.UpdateUserRequestDto;
import com.study.apigateway.dto.User.response.ListUserResponseDto;
import com.study.apigateway.dto.User.response.UserResponseDto;
import com.study.apigateway.exception.ErrorResponse;
import com.study.apigateway.service.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public Mono<ResponseEntity<UserResponseDto>> createUser(@Valid @RequestBody CreateUserRequestDto request) {
        return userService.createUser(request).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user's information by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<UserResponseDto>> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for users by username.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    public Mono<ResponseEntity<ListUserResponseDto>> searchUsersByUsername(@RequestParam String keyword,
                                                                           @RequestParam(required = false) UUID cursor,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return userService.searchUserByUsername(keyword, cursor, size).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user's information.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<UserResponseDto>> updateUser(@PathVariable UUID id,
                                                            @Valid @RequestBody UpdateUserRequestDto request) {
        return userService.updateUser(id, request).map(ResponseEntity::ok);
    }
}
