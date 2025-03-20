package com.study.apigateway.dto.User.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.common.enums.Gender;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UpdateUserRequest {
    @Size(min =3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String username;

    @Past(message = "Date of birth must be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Gender gender;

    private String avatarUrl;
}
