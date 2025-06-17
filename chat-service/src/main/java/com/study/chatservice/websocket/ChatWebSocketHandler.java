package com.study.chatservice.websocket;

import com.study.chatservice.dto.response.ConnectedUser;
import com.study.common.exceptions.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    //Save sessions -> connected users
    private final Map<WebSocketSession, ConnectedUser> sessionUserMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        UUID userId = extractUUIDQueryParam(session, "userId");
        UUID teamId = extractUUIDQueryParam(session, "teamId");

        if (userId != null && teamId != null) {
            sessionUserMap.put(session, new ConnectedUser(userId, teamId));
            System.out.println("User " + userId + " connected to team " + teamId);
        } else {
            try {
                session.close(CloseStatus.BAD_DATA);
            } catch (IOException e) {
                throw new BusinessException("Error while connecting to websocket: " + e.getMessage());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionUserMap.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }

    public void sendMessageToOnlineMembers(UUID teamId, String message) {
        sessionUserMap.forEach((session, user) -> {
            if (user.getTeamId().equals(teamId) && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean isUserInTeam(UUID userId, UUID teamId) {
        return sessionUserMap.values().stream()
                .anyMatch(u -> u.getUserId().equals(userId) && u.getTeamId().equals(teamId));
    }

    private UUID extractUUIDQueryParam(WebSocketSession session, String key) {
        URI uri = session.getUri();
        if (uri == null || uri.getQuery() == null) return null;

        Map<String, String> paramMap = Arrays.stream(uri.getQuery().split("&"))
                .map(s -> s.split("="))
                .filter(arr -> arr.length == 2)
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));

        try {
            return UUID.fromString(paramMap.get(key));
        } catch (Exception e) {
            return null;
        }
    }
}

