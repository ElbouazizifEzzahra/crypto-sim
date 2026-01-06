package com.cryptosim.crypto_sim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trades")
public class Trades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long buyerId;
    private Long sellerId;
    //How much was traded?
    private BigDecimal amount;
    private BigDecimal price;
    @Column(name = "trade_time")
    private Timestamp currentTimestamp;
    @ManyToOne
    @JoinColumn
    private Transaction buyerOrder;

    @ManyToOne
    @JoinColumn
    private Transaction sellerOrder;


}
