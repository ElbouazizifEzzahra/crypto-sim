package com.cryptosim.crypto_sim.model;

import com.cryptosim.crypto_sim.service.Price.PriceService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// model/Wallet.java
@Entity
@Table(name = "wallets")
@Data
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "usd_balance", precision = 19, scale = 8)
    private BigDecimal usdBalance = new BigDecimal("10000.00");

    @Column(name = "btc_balance", precision = 19, scale = 8)
    private BigDecimal btcBalance = BigDecimal.ZERO;

    @Column(name = "eth_balance", precision = 19, scale = 8)
    private BigDecimal ethBalance = BigDecimal.ZERO;

    @Column(name = "sol_balance", precision = 19, scale = 8)
    private BigDecimal solBalance = BigDecimal.ZERO;

    // AJOUTE si tu as besoin de locked
    @Column(name = "btc_locked", precision = 19, scale = 8)
    private BigDecimal btcLocked = BigDecimal.ZERO;

    @Column(name = "eth_locked", precision = 19, scale = 8)
    private BigDecimal ethLocked = BigDecimal.ZERO;

    @Column(name = "sol_locked", precision = 19, scale = 8)
    private BigDecimal solLocked = BigDecimal.ZERO;

    // GETTERS/SETTERS pour locked
    public BigDecimal getBtcLocked() { return btcLocked; }
    public void setBtcLocked(BigDecimal btcLocked) { this.btcLocked = btcLocked; }

    public BigDecimal getEthLocked() { return ethLocked; }
    public void setEthLocked(BigDecimal ethLocked) { this.ethLocked = ethLocked; }

    public BigDecimal getSolLocked() { return solLocked; }
    public void setSolLocked(BigDecimal solLocked) { this.solLocked = solLocked; }

    // Méthode utilitaire pour getLocked par symbole
    public BigDecimal getLocked(String symbol) {
        return switch (symbol.toUpperCase()) {
            case "BTC" -> btcLocked;
            case "ETH" -> ethLocked;
            case "SOL" -> solLocked;
            default -> BigDecimal.ZERO;
        };
    }

    public void setLocked(String symbol, BigDecimal amount) {
        switch (symbol.toUpperCase()) {
            case "BTC" -> btcLocked = amount;
            case "ETH" -> ethLocked = amount;
            case "SOL" -> solLocked = amount;
        }
    }

    // Méthode pour getBalance (si besoin)
    public BigDecimal getBalance(String symbol) {
        return switch (symbol.toUpperCase()) {
            case "BTC" -> btcBalance;
            case "ETH" -> ethBalance;
            case "SOL" -> solBalance;
            case "USD" -> usdBalance;
            default -> BigDecimal.ZERO;
        };
    }

    public void setBalance(String symbol, BigDecimal amount) {
        switch (symbol.toUpperCase()) {
            case "BTC" -> btcBalance = amount;
            case "ETH" -> ethBalance = amount;
            case "SOL" -> solBalance = amount;
            case "USD" -> usdBalance = amount;
        }
    }
}