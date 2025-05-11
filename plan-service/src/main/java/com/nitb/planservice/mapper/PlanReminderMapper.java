package com.nitb.planservice.mapper;

import com.nitb.planservice.entity.PlanReminder;
import com.study.planservice.grpc.PlanReminderResponse;
import com.study.planservice.grpc.PlanRemindersResponse;

import java.util.List;

public class PlanReminderMapper {
    private PlanReminderMapper() {}

    public static PlanReminderResponse toPlanReminderResponse(PlanReminder reminder) {
        return PlanReminderResponse.newBuilder()
                .setId(reminder.getId().toString())
                .setRemindAt(reminder.getRemindAt().toString())
                .build();
    }

    public static PlanRemindersResponse toPlanRemindersResponse(List<PlanReminder> reminders) {
        List<PlanReminderResponse> responses = reminders.stream()
                .map(PlanReminderMapper::toPlanReminderResponse)
                .toList();

        return PlanRemindersResponse.newBuilder()
                .addAllReminder(responses)
                .build();
    }
}
