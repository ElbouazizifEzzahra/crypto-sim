package com.cryptosim.crypto_sim.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticker {
    private String symbol;           // BTC, ETH, etc.
    private BigDecimal price;        // Prix actuel
    private BigDecimal change;       // Changement ($)
    private BigDecimal changePercent; // Changement (%)
    private BigDecimal high24h;      // Plus haut (24h)
    private BigDecimal low24h;       // Plus bas (24h)
    private BigDecimal volume24h;    // Volume (24h)
    private BigDecimal bid;          // Meilleur bid
    private BigDecimal ask;          // Meilleur ask
    private Long timestamp;          // Timestamp
}