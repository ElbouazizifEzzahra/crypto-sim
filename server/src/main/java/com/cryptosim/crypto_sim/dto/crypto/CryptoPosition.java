package com.cryptosim.crypto_sim.dto.crypto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CryptoPosition {
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal averageBuyPrice;
    private BigDecimal currentPrice;
    private BigDecimal currentValue;
    private BigDecimal unrealizedPnL;
    private BigDecimal unrealizedPnLPercent;

    public CryptoPosition(String symbol) {
        this.symbol = symbol;
        this.quantity = BigDecimal.ZERO;
        this.averageBuyPrice = BigDecimal.ZERO;
    }

    public void addBuy(BigDecimal quantity, BigDecimal price) {
        BigDecimal totalCost = this.quantity.multiply(this.averageBuyPrice)
                .add(quantity.multiply(price));

        this.quantity = this.quantity.add(quantity);
        this.averageBuyPrice = this.quantity.compareTo(BigDecimal.ZERO) > 0
                ? totalCost.divide(this.quantity, 4, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;
    }

    public void addSell(BigDecimal quantity, BigDecimal price) {
        this.quantity = this.quantity.subtract(quantity);
        if (this.quantity.compareTo(BigDecimal.ZERO) < 0) {
            this.quantity = BigDecimal.ZERO;
        }
    }
}