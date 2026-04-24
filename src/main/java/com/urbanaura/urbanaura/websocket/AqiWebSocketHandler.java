package com.urbanaura.urbanaura.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class AqiWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final Random random = new Random();
    private final String[] zones = {"Kothrud", "Baner", "Hinjawadi"};
    private ScheduledExecutorService scheduler;

    public AqiWebSocketHandler() {
        startAqiSimulation();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        // Send initial pulse
        session.sendMessage(generatePayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    private void startAqiSimulation() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        // Broadcast an AQI fluctuation every 4 seconds to satisfy CO4
        scheduler.scheduleAtFixedRate(() -> {
            TextMessage message = generatePayload();
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        }, 4, 4, TimeUnit.SECONDS);
    }

    private TextMessage generatePayload() {
        String zone = zones[random.nextInt(zones.length)];
        // Generate an AQI between 40 and 160
        int aqi = 40 + random.nextInt(120);
        String json = String.format("{\"zone\": \"%s\", \"aqi\": %d}", zone, aqi);
        return new TextMessage(json);
    }
}
