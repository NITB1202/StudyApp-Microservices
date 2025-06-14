package com.study.teamservice.event;

import com.study.common.enums.TeamRole;
import com.study.common.events.Notification.InvitationAcceptEvent;
import com.study.teamservice.service.MemberService;
import com.study.teamservice.service.TeamNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamEventListener {
    private final MemberService memberService;
    private final TeamNotificationService teamNotificationService;

    @KafkaListener(topics = "invitation-accepted", groupId = "team-invitation-accepted")
    public void consumeInvitationAcceptEvent(InvitationAcceptEvent event) {
        UUID userId = event.getUserId();
        UUID teamId = event.getTeamId();

        memberService.saveMember(teamId, userId, TeamRole.MEMBER);
        teamNotificationService.publishUserJoinedTeamNotification(userId, teamId);
    }
}
