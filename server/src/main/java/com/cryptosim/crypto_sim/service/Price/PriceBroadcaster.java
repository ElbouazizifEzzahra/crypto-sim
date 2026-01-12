package com.cryptosim.crypto_sim.service.Price;



import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PriceBroadcaster {

    private final PriceService priceService;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 1000)
    public void broadcastPrices() {
        Map<String, BigDecimal> currentPrices = priceService.getAllPrices();

        if (!currentPrices.isEmpty()) {

            messagingTemplate.convertAndSend("/topic/ticker", currentPrices);


            currentPrices.forEach((symbol, price) -> {
                Map<String, Object> tickerData = new HashMap<>();
                tickerData.put("symbol", symbol);
                tickerData.put("price", price);
                tickerData.put("timestamp", System.currentTimeMillis());

                messagingTemplate.convertAndSend("/topic/ticker/" + symbol, (Object) tickerData);
            });
        }
    }
}