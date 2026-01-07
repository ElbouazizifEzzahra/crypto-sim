package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.service.Price.PriceService;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/prices")
//@CrossOrigin(origins = "http://localhost:5173") // Autorise ton React
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

    // Récupérer TOUS les prix d'un coup (Utile pour le dashboard)
    @GetMapping("/all")
    public Map<String, BigDecimal> getAllPrices() {
        // Tu dois ajouter une méthode getAllPrices() dans ton PriceService
        // qui retourne simplement la ConcurrentHashMap
        return priceService.getAllPrices();
    }
}