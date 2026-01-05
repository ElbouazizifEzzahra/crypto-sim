package com.cryptosim.crypto_sim.service.Price;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PriceServiceImp implements PriceService {

    // In-Memory Storage (The "Hub")
    // ConcurrentHashMap is thread-safe for high-speed updates
    private final ConcurrentHashMap<String, BigDecimal> priceStore = new ConcurrentHashMap<>();

    @Override
    public void updatePrice(String symbol, BigDecimal price) {
        priceStore.put(symbol.toUpperCase(), price);
    }

    @Override
    public BigDecimal getCurrentPrice(String symbol) {
        return priceStore.getOrDefault(symbol.toUpperCase(), BigDecimal.ZERO);
    }
}