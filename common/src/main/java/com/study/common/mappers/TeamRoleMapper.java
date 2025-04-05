package com.study.common.mappers;

import com.study.common.enums.TeamRole;

public class TeamRoleMapper {
    private TeamRoleMapper() {}

    public static TeamRole toEnum(com.study.teamservice.grpc.TeamRole role){
        return switch (role){
            case CREATOR -> TeamRole.CREATOR;
            case ADMIN -> TeamRole.ADMIN;
            default -> TeamRole.MEMBER;
        };
    }

    public static com.study.teamservice.grpc.TeamRole toProtoEnum(TeamRole role) {
        return switch (role) {
            case CREATOR -> com.study.teamservice.grpc.TeamRole.CREATOR;
            case ADMIN -> com.study.teamservice.grpc.TeamRole.ADMIN;
            default -> com.study.teamservice.grpc.TeamRole.MEMBER;
        };
    }
}
