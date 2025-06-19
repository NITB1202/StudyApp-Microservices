package com.study.common.mappers;


import com.study.common.enums.UserRole;
import com.study.common.exceptions.BusinessException;

public class UserRoleMapper {
    private UserRoleMapper() {}

    public static UserRole fromString(String role) {
        return switch (role) {
            case "ADMIN" -> UserRole.ADMIN;
            case "USER" -> UserRole.USER;
            default -> throw new BusinessException("Unsupported role: " + role);
        };
    }
}
