package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.service.Price.PriceService;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    // Récupérer le prix d'une crypto spécifique (ex: /api/prices/BTC)
    @GetMapping("/{symbol}")
    public BigDecimal getPrice(@PathVariable String symbol) {
        return priceService.getCurrentPrice(symbol);
    }

    @GetMapping("/all")
    public Map<String, BigDecimal> getAllPrices() {
        return priceService.getAllPrices();
    }
}