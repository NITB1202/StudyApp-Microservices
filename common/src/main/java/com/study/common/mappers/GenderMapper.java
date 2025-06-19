package com.study.common.mappers;

import com.study.common.enums.Gender;

public class GenderMapper {
    //Private constructor to prevent initializing object
    private GenderMapper() {}

    public static Gender toEnum(com.study.userservice.grpc.Gender protoGender) {
        return switch (protoGender) {
            case MALE -> Gender.MALE;
            case FEMALE -> Gender.FEMALE;
            default -> Gender.UNSPECIFIED;
        };
    }

    public static com.study.userservice.grpc.Gender toProtoEnum(Gender gender) {
        return switch (gender) {
            case MALE -> com.study.userservice.grpc.Gender.MALE;
            case FEMALE -> com.study.userservice.grpc.Gender.FEMALE;
            default -> com.study.userservice.grpc.Gender.UNSPECIFIED;
        };
    }

    public static com.study.userservice.grpc.Gender fromString(String gender) {
        String handledGender = gender.toUpperCase();

        return switch (handledGender) {
            case "MALE" -> com.study.userservice.grpc.Gender.MALE;
            case "FEMALE" -> com.study.userservice.grpc.Gender.FEMALE;
            default -> com.study.userservice.grpc.Gender.UNSPECIFIED;
        };
    }
}
