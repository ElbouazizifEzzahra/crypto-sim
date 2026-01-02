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
public class Trades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long buyerId;
    private Long sellerId;
    //How much was traded?
    private BigDecimal amount;
    private BigDecimal price;
    private Timestamp currentTimestamp;
    @ManyToOne
    @JoinColumn
    private Orders buyerOrder;

    @ManyToOne
    @JoinColumn
    private Orders sellerOrder;
    

}
