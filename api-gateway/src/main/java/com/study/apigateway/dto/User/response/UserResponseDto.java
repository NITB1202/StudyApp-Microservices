package com.study.apigateway.dto.User.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.userservice.grpc.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;

    private String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Gender gender;

    private String avatarUrl;
}
