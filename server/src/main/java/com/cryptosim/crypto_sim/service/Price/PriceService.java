package com.cryptosim.crypto_sim.service.Price;

import jakarta.annotation.PostConstruct;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// service/price/PriceService.java
@Service
public class PriceService {

    // ConcurrentHashMap comme spécifié dans le PDF
    private final ConcurrentHashMap<String, BigDecimal> priceMap = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;

    public PriceService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        initializePrices();
    }

    @PostConstruct
    public void init() {
        initializePrices();
    }

    private void initializePrices() {
        priceMap.put("BTC", new BigDecimal("45000.00"));
        priceMap.put("ETH", new BigDecimal("2500.00"));
        priceMap.put("SOL", new BigDecimal("100.00"));
    }

    public void updatePrice(String symbol, BigDecimal price) {
        priceMap.put(symbol.toUpperCase(), price);
        priceMap.forEach((s, p) -> System.out.println("  " + s + ": $" + p));
    }
    public BigDecimal getCurrentPrice(String symbol) {
        return priceMap.getOrDefault(symbol.toUpperCase(), BigDecimal.ZERO);
    }

    public Map<String, BigDecimal> getAllPrices() {
        return new HashMap<>(priceMap);
    }


    public BigDecimal calculateCryptoValue(String symbol, BigDecimal quantity) {
        BigDecimal price = getCurrentPrice(symbol);
        return price.multiply(quantity);
    }
}

