package com.study.common.mappers;


import com.study.common.enums.LinkedSubject;

public class LinkedSubjectMapper {
    private LinkedSubjectMapper() {}

    public static com.study.notificationservice.grpc.LinkedSubject toGrpcEnum(LinkedSubject subject) {
        return switch (subject) {
            case PLAN -> com.study.notificationservice.grpc.LinkedSubject.PLAN;
            case TEAM -> com.study.notificationservice.grpc.LinkedSubject.TEAM;
            case INVITATION -> com.study.notificationservice.grpc.LinkedSubject.INVITATION;
        };
    }
}
