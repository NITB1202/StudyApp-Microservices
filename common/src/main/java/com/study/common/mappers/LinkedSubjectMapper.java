package com.study.common.mappers;


import com.study.common.enums.LinkedSubject;
import com.study.common.exceptions.NotFoundException;

public class LinkedSubjectMapper {
    private LinkedSubjectMapper() {}

    public static com.study.notificationservice.grpc.LinkedSubject toGrpcEnum(LinkedSubject subject) {
        return switch (subject) {
            case PLAN -> com.study.notificationservice.grpc.LinkedSubject.PLAN;
            case TEAM -> com.study.notificationservice.grpc.LinkedSubject.TEAM;
            case INVITATION -> com.study.notificationservice.grpc.LinkedSubject.INVITATION;
        };
    }

    public static LinkedSubject toEnum(com.study.notificationservice.grpc.LinkedSubject subject) {
        return switch (subject) {
            case PLAN -> LinkedSubject.PLAN;
            case TEAM -> LinkedSubject.TEAM;
            case INVITATION -> LinkedSubject.INVITATION;
            default -> throw new NotFoundException("Subject not found.");
        };
    }
}
