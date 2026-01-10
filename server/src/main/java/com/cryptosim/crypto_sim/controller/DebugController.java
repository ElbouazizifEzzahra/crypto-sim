package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.service.Price.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DebugController {
    private final PriceService priceService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/api/debug/prices")
    public String debugPrices() {
        Map<String, BigDecimal> prices = priceService.getAllPrices();
        return "Prix stockés: " + prices;
    }
    @GetMapping("/api/force-send")
    public String forceSend() {
        Map<String, BigDecimal> prices = priceService.getAllPrices();
        messagingTemplate.convertAndSend("/topic/ticker", prices);
        return "Forcé l'envoi: " + prices;
    }
}