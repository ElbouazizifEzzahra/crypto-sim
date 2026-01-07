package com.cryptosim.crypto_sim.dto;

import com.cryptosim.crypto_sim.service.trade.OrderService;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class LimitOrderRequest {
    private String symbol;              // BTC, ETH, SOL, etc.
    private OrderService.OrderType orderType;  // LIMIT_BUY ou LIMIT_SELL
    private BigDecimal targetPrice;     // Prix cible
    private BigDecimal quantity;        // Quantité
}