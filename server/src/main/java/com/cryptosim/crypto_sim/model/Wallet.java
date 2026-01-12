package com.cryptosim.crypto_sim.model;


import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;


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


    @Column(name = "btc_locked", precision = 19, scale = 8)
    private BigDecimal btcLocked = BigDecimal.ZERO;

    @Column(name = "eth_locked", precision = 19, scale = 8)
    private BigDecimal ethLocked = BigDecimal.ZERO;

    @Column(name = "sol_locked", precision = 19, scale = 8)
    private BigDecimal solLocked = BigDecimal.ZERO;

    
    public void setBtcLocked(BigDecimal btcLocked) { this.btcLocked = btcLocked; }


    public void setEthLocked(BigDecimal ethLocked) { this.ethLocked = ethLocked; }

    public void setSolLocked(BigDecimal solLocked) { this.solLocked = solLocked; }



}