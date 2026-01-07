package com.cryptosim.crypto_sim.dto.portfolio;

import com.cryptosim.crypto_sim.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class PortfolioResponse {
    private BigDecimal totalNetWorth;
    private BigDecimal usdBalance;
    private List<HoldingDTO> holdings;
    private List<Wallet> wallets;

    public PortfolioResponse(List<Wallet> wallets, BigDecimal totalNetWorth) {
        this.wallets = wallets;
        this.totalNetWorth = totalNetWorth;
    }

    @Data
    @AllArgsConstructor
    public static class HoldingDTO {
        private String symbol;
        private BigDecimal quantity;
        private BigDecimal currentPrice;
        private BigDecimal totalValue; // quantity * currentPrice
    }
}