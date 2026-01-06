/*
package com.cryptosim.crypto_sim.service.portfolio;

import com.cryptosim.crypto_sim.model.Side;
import com.cryptosim.crypto_sim.model.Transaction;
import com.cryptosim.crypto_sim.model.Wallet;
import com.cryptosim.crypto_sim.repository.TransactionRepository;
import com.cryptosim.crypto_sim.repository.WalletRepository;
import com.cryptosim.crypto_sim.service.Price.PriceService;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private WalletRepository walletRepo;

    @Autowired
    private PriceService priceService;

    public PortfolioSummary getPortfolioSummary(String userId) {
        List<Transaction> transactions = transactionRepo.findByUserId(userId);
        Wallet wallet = walletRepo.findByUserId(userId).orElse(new Wallet());

        // Calcul P&L (Profit & Loss)
        BigDecimal totalInvested = BigDecimal.ZERO;
        BigDecimal currentValue = BigDecimal.ZERO;
        Map<String, CryptoPosition> positions = new HashMap<>();

        for (Transaction tx : transactions) {
            String symbol = tx.getSymbol();
            CryptoPosition position = positions.getOrDefault(symbol, new CryptoPosition(symbol));

            if (tx.getType() == Side.BUY) {
                position.addBuy(tx.getQuantity(), tx.getPrice());
                totalInvested = totalInvested.add(tx.getQuantity().multiply(tx.getPrice()));
            } else {
                position.addSell(tx.getQuantity(), tx.getPrice());
            }

            positions.put(symbol, position);
        }

        // Calcul valeur actuelle
        for (CryptoPosition pos : positions.values()) {
            BigDecimal currentPrice = priceService.getCurrentPrice(pos.getSymbol());
            if (currentPrice != null && pos.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal positionValue = pos.getQuantity().multiply(currentPrice);
                currentValue = currentValue.add(positionValue);
                pos.setCurrentValue(positionValue);
                pos.setCurrentPrice(currentPrice);
            }
        }

        BigDecimal totalValue = currentValue.add(wallet.getUsdBalance());
        BigDecimal totalPnL = totalValue.subtract(totalInvested);
        BigDecimal pnlPercent = totalInvested.compareTo(BigDecimal.ZERO) > 0
                ? totalPnL.divide(totalInvested, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        return PortfolioSummary.builder()
                .userId(userId)
                .totalInvested(totalInvested)
                .currentValue(totalValue)
                .profitLoss(totalPnL)
                .profitLossPercent(pnlPercent)
                .positions(new ArrayList<>(positions.values()))
                .usdBalance(wallet.getUsdBalance())
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    @Data
    @Builder
    public static class PortfolioSummary {
        private String userId;
        private BigDecimal totalInvested;
        private BigDecimal currentValue;
        private BigDecimal profitLoss;
        private BigDecimal profitLossPercent;
        private List<CryptoPosition> positions;
        private BigDecimal usdBalance;
        private LocalDateTime lastUpdated;
    }

    @Data
    public static class CryptoPosition {
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
                    ? totalCost.divide(this.quantity, 4, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
        }

        public void addSell(BigDecimal quantity, BigDecimal price) {
            this.quantity = this.quantity.subtract(quantity);
            if (this.quantity.compareTo(BigDecimal.ZERO) < 0) {
                this.quantity = BigDecimal.ZERO;
            }
        }
    }
}*/
