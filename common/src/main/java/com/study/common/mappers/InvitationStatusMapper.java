package com.study.common.mappers;

import com.study.common.enums.InvitationStatus;

public class InvitationStatusMapper {
    private InvitationStatusMapper() {}

    public static com.study.teamservice.grpc.InvitationStatus toProtoEnum(InvitationStatus status) {
        return switch (status){
            case PENDING -> com.study.teamservice.grpc.InvitationStatus.PENDING;
            case ACCEPTED -> com.study.teamservice.grpc.InvitationStatus.ACCEPTED;
            case DECLINED -> com.study.teamservice.grpc.InvitationStatus.DECLINED;
        };
    }

    public static InvitationStatus toEnum(com.study.teamservice.grpc.InvitationStatus status) {
        return switch (status){
            case PENDING, UNRECOGNIZED -> InvitationStatus.PENDING;
            case ACCEPTED -> InvitationStatus.ACCEPTED;
            case DECLINED -> InvitationStatus.DECLINED;
        };
    }
}
