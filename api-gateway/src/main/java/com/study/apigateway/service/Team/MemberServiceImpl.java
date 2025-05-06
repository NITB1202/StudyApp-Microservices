package com.study.apigateway.service.Team;

import com.study.apigateway.dto.Action.ActionResponseDto;
import com.study.apigateway.dto.Notification.request.CreateInvitationRequestDto;
import com.study.apigateway.dto.Team.request.RemoveTeamMemberRequestDto;
import com.study.apigateway.dto.Team.request.UpdateMemberRoleRequestDto;
import com.study.apigateway.dto.Team.response.ListTeamMemberResponseDto;
import com.study.apigateway.dto.Team.response.TeamMemberResponseDto;
import com.study.apigateway.dto.Team.response.TeamUserProfileResponseDto;
import com.study.apigateway.grpc.TeamServiceGrpcClient;
import com.study.apigateway.grpc.UserServiceGrpcClient;
import com.study.apigateway.mapper.ActionMapper;
import com.study.apigateway.mapper.TeamMemberMapper;
import com.study.common.grpc.ActionResponse;
import com.study.common.utils.CursorUtil;
import com.study.common.utils.DecodedCursor;
import com.study.teamservice.grpc.AllTeamMembersResponse;
import com.study.teamservice.grpc.ListTeamMembersResponse;
import com.study.teamservice.grpc.TeamMemberResponse;
import com.study.teamservice.grpc.TeamMemberSummaryResponse;
import com.study.userservice.grpc.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final TeamServiceGrpcClient teamGrpcClient;
    private final UserServiceGrpcClient userGrpcClient;

    @Override
    public Mono<ActionResponseDto> createInvitation(UUID userId, CreateInvitationRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = teamGrpcClient.createInvitation(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> joinTeam(UUID userId, String teamCode) {
        return Mono.fromCallable(()->{
            ActionResponse response = teamGrpcClient.joinTeam(userId, teamCode);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TeamUserProfileResponseDto> getUserInTeam(UUID userId, UUID teamId) {
        return Mono.fromCallable(()->{
            TeamMemberResponse member = teamGrpcClient.getTeamMember(userId, teamId);
            return TeamMemberMapper.toTeamUserProfileResponseDto(member);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ListTeamMemberResponseDto> getTeamMembers(UUID teamId, String cursor, int size) {
        return Mono.fromCallable(()->{
            ListTeamMembersResponse response = teamGrpcClient.getTeamMembers(teamId, cursor, size);

            List<TeamMemberResponseDto> members = new ArrayList<>();
            for(TeamMemberSummaryResponse teamUser : response.getMembersList()) {
                UserDetailResponse user = userGrpcClient.getUserById(UUID.fromString(teamUser.getUserId()));
                TeamMemberResponseDto dto = TeamMemberMapper.toTeamMemberResponseDto(teamUser, user);
                members.add(dto);
            }

            return TeamMemberMapper.toListTeamMemberResponseDto(members, response.getTotal(), response.getNextCursor());

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ListTeamMemberResponseDto> searchTeamMembersByUsername(UUID teamId, String keyword, String cursor, int size) {
        return Mono.fromCallable(() -> {
            AllTeamMembersResponse response = teamGrpcClient.getAllTeamMembers(teamId);

            String handledKeyword = keyword.trim().toLowerCase();
            List<TeamMemberResponseDto> paginated = new ArrayList<>();

            // Parse cursor
            LocalDate cursorJoinDate = null;
            UUID cursorId = null;
            if (cursor != null && !cursor.isEmpty()) {
                DecodedCursor decodedCursor = CursorUtil.decodeCursor(cursor);
                cursorJoinDate = decodedCursor.getDate();
                cursorId = decodedCursor.getId();
            }

            // Sort by joinDate DESC, id ASC
            List<TeamMemberResponse> sorted = response.getMembersList().stream()
                    .sorted(Comparator
                            .comparing((TeamMemberResponse m) -> LocalDate.parse(m.getJoinDate())).reversed()
                            .thenComparing(m -> UUID.fromString(m.getUserId())))
                    .toList();

            for (TeamMemberResponse teamUser : sorted) {
                LocalDate joinDate = LocalDate.parse(teamUser.getJoinDate());
                UUID userId = UUID.fromString(teamUser.getUserId());

                UserDetailResponse user = userGrpcClient.getUserById(userId);
                if (!user.getUsername().toLowerCase().contains(handledKeyword)) {
                    continue;
                }

                // Apply cursor
                if (cursorJoinDate != null) {
                    if (joinDate.isAfter(cursorJoinDate)) continue;
                    if (joinDate.isEqual(cursorJoinDate) && userId.compareTo(cursorId) <= 0) continue;
                }

                TeamMemberResponseDto dto = TeamMemberMapper.toTeamMemberResponseDto(teamUser, user);
                paginated.add(dto);

                if (paginated.size() == size) break;
            }

            int total = (int) response.getMembersList().stream()
                    .map(teamUser -> userGrpcClient.getUserById(UUID.fromString(teamUser.getUserId())))
                    .filter(user -> user.getUsername().toLowerCase().contains(handledKeyword))
                    .count();

            // Calculate next cursor
            String nextCursor = null;
            if (paginated.size() == size) {
                TeamMemberResponseDto lastMember = paginated.get(paginated.size() - 1);
                TeamMemberResponse info = teamGrpcClient.getTeamMember(lastMember.getUserId(), teamId);
                nextCursor = CursorUtil.encodeCursor(LocalDate.parse(info.getJoinDate()), UUID.fromString(info.getUserId()));
            }

            return TeamMemberMapper.toListTeamMemberResponseDto(paginated, total, nextCursor);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> updateTeamMemberRole(UUID userId, UpdateMemberRoleRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = teamGrpcClient.updateTeamMemberRole(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> removeTeamMember(UUID userId, RemoveTeamMemberRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = teamGrpcClient.removeTeamMember(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> leaveTeam(UUID userId, UUID teamId) {
        return Mono.fromCallable(()->{
            ActionResponse response = teamGrpcClient.leaveTeam(userId, teamId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
