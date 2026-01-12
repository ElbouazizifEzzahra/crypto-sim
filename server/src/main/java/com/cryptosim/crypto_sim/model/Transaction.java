package com.cryptosim.crypto_sim.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Side type; // BUY or SELL

    @Column(nullable = false)
    private String symbol; // BTC, ETH, SOL

    @Column(precision = 19, scale = 8)
    private BigDecimal quantity;

    @Column(name = "price_at_transaction", precision = 19, scale = 8)
    @JsonProperty("price")
    private BigDecimal priceAtTransaction;

    @Column(name = "total_amount", precision = 19, scale = 8)
    private BigDecimal totalAmount;

    @CreationTimestamp
    private LocalDateTime timestamp;
}


