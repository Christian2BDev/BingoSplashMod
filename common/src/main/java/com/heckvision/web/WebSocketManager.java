package com.heckvision.web;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.util.concurrent.*;

public class WebSocketManager {
    private final URI serverUri;
    private WebSocketClient client;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private boolean shouldConnect = false;
    private boolean connecting = false;

    private MessageListener messageListener;

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    public WebSocketManager(String serverUrl) {
        this.serverUri = URI.create(serverUrl);
    }


    public void setShouldConnect(boolean shouldConnect) {
        this.shouldConnect = shouldConnect;

        if (shouldConnect) {
            tryStartConnection();
        } else {
            disconnect();
        }
    }

    private void tryStartConnection() {
        if (client != null && client.getReadyState() == ReadyState.OPEN) return;
        if (connecting) return;

        connecting = true;

        executor.execute(() -> {
            try {
                System.out.println("Attempting to connect to WebSocket...");
                WebSocketClient socket = new WebSocketClient(serverUri) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        System.out.println("WebSocket connected.");
                    }

                    @Override
                    public void onMessage(String message) {
                        System.out.println("Received: " + message);
                        if (messageListener != null) {
                            messageListener.onMessage(message);
                        }
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        System.out.println("WebSocket closed: " + reason);
                        client = null;
                        connecting = false;
                        if (shouldConnect) {
                            scheduleReconnect();
                        }
                    }

                    @Override
                    public void onError(Exception ex) {
                        System.err.println("WebSocket error:");
                        ex.printStackTrace();
                        client = null;
                        connecting = false;
                        if (shouldConnect) {
                            scheduleReconnect();
                        }
                    }
                };

                SSLContext sslContext = SSLContext.getDefault();
                socket.setSocketFactory(sslContext.getSocketFactory());

                client = socket;
                socket.connect(); // Non-blocking
            } catch (Exception e) {
                System.err.println("WebSocket connect exception:");
                e.printStackTrace();
                connecting = false;
                scheduleReconnect();
            }
        });
    }

    private void scheduleReconnect() {
        executor.schedule(this::tryStartConnection, 5, TimeUnit.SECONDS);
    }

    private void disconnect() {
        if (client != null && (client.getReadyState() == ReadyState.OPEN || client.getReadyState() == ReadyState.NOT_YET_CONNECTED)) {
            try {
                client.close();
                System.out.println("WebSocket disconnected by request.");
            } catch (Exception e) {
                System.err.println("WebSocket disconnect exception:");
                e.printStackTrace();
            }
        }
        client = null;
    }

    public void shutdown() {
        shouldConnect = false;
        disconnect();
        executor.shutdownNow();
    }
}