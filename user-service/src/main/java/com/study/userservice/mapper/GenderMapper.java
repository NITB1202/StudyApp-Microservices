package com.study.userservice.mapper;

import com.study.common.enums.Gender;

public class GenderMapper {
    //Private constructor to prevent initializing object
    private GenderMapper() {}

    public static Gender toEnum(com.study.userservice.grpc.Gender protoGender) {
        return switch (protoGender) {
            case MALE -> Gender.MALE;
            case FEMALE -> Gender.FEMALE;
            default -> Gender.OTHER;
        };
    }

    public static com.study.userservice.grpc.Gender toProtoEnum(Gender gender) {
        return switch (gender) {
            case MALE -> com.study.userservice.grpc.Gender.MALE;
            case FEMALE -> com.study.userservice.grpc.Gender.FEMALE;
            default -> com.study.userservice.grpc.Gender.OTHER;
        };
    }
}
