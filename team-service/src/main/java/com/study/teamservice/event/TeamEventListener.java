package com.study.teamservice.event;

import com.study.common.events.Notification.InvitationAcceptEvent;
import com.study.teamservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamEventListener {
    private MemberService memberService;

    @KafkaListener(topics = "invitation-accepted", groupId = "team-service-group")
    public void consumeInvitationAcceptEvent(InvitationAcceptEvent event) {
        memberService.acceptInvitation(event);
    }
}
