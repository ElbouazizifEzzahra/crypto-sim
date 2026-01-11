
package com.cryptosim.crypto_sim.service.webSocket;

import com.cryptosim.crypto_sim.service.Price.PriceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

// service/price/BinanceWebSocketClient.java
@Service
@Slf4j
@RequiredArgsConstructor
public class BinanceWebSocketClient {

    private WebSocketClient webSocketClient;
    private final PriceService priceService;
    private final SimpMessagingTemplate messagingTemplate;

    // Symboles supportés selon le PDF
    private static final List<String> SYMBOLS = Arrays.asList("BTCUSDT", "ETHUSDT", "SOLUSDT");

    @PostConstruct
    public void init() {
        connectToBinance();
    }

    public void connectToBinance() {
        try {
            // Créer le stream URL comme dans le PDF
            String streams = SYMBOLS.stream()
                    .map(s -> s.toLowerCase() + "@ticker")
                    .collect(Collectors.joining("/"));

            String url = "wss://stream.binance.com:9443/stream?streams=" + streams;

            webSocketClient = new WebSocketClient(new URI(url)) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    log.info("✅ Connected to Binance WebSocket");
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("BINANCE: " + message);
                    processBinanceMessage(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.warn("❌ Disconnected from Binance: {}", reason);
                    scheduleReconnection();
                }

                @Override
                public void onError(Exception ex) {
                    log.error("Binance WebSocket error: {}", ex.getMessage());
                }
            };

            webSocketClient.connect();

        } catch (Exception e) {
            log.error("Failed to connect to Binance: {}", e.getMessage());
            scheduleReconnection();
        }
    }


    private void processBinanceMessage(String message) {
        try {


            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(message);

            JsonNode data = root.get("data");
            if (data != null) {
                String symbol = data.get("s").asText(); // "BTCUSDT"
                String baseSymbol = symbol.replace("USDT", ""); // "BTC"
                BigDecimal price = new BigDecimal(data.get("c").asText());
                priceService.updatePrice(baseSymbol, price);
                System.out.println("💰 " + symbol + " = $" + price);
            }
        } catch (Exception e) {
            System.out.println(" ERREUR PARSING: " + e.getMessage());
        }
    }
    private void scheduleReconnection() {
        log.info("🔄 Reconnecting in 5 seconds...");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                connectToBinance();
            }
        }, 5000);
    }
}

