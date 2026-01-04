package com.cryptosim.crypto_sim.service.Price;

// 1. Standard Imports
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

// 2. WebSocket Imports
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class BinanceStreamService extends TextWebSocketHandler {

    private final PriceService priceService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    // Manual Constructor to ensure Spring injects the Beans correctly
    public BinanceStreamService(PriceService priceService, SimpMessagingTemplate messagingTemplate) {
        this.priceService = priceService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void connectToBinance() {
        // Create the client

        StandardWebSocketClient client = new StandardWebSocketClient();

        // Binance Stream URL
        String binanceUrl = "wss://stream.binance.com:9443/ws/btcusdt@trade/ethusdt@trade/solusdt@trade";

        try {
            // Initiate the connection
            client.doHandshake(this, binanceUrl);
            System.out.println("✅ CONNECTED to Binance! Listening for trades...");
        } catch (Exception e) {
            System.err.println("❌ CONNECTION ERROR: Could not connect to Binance. " + e.getMessage());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            JsonNode node = mapper.readTree(payload);

            // Extract Data
            String symbolRaw = node.get("s").asText(); // e.g., "BTCUSDT"
            String symbol = symbolRaw.replace("USDT", "");
            BigDecimal price = new BigDecimal(node.get("p").asText());

            // 1. Update Database/RAM
            priceService.updatePrice(symbol, price);

            // 2. Broadcast to Frontend
            Map<String, Object> update = Map.of("symbol", symbol, "price", price);
            messagingTemplate.convertAndSend("/topic/ticker", update);

        } catch (Exception e) {
            // Silently ignore parse errors to keep the console clean
        }
    }
}