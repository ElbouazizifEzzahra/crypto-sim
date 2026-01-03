package com.cryptosim.crypto_sim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private Side typeOrder;
    @Enumerated(value = EnumType.STRING)
    private Status statutOrder;
    private BigDecimal quantity ;
    private Timestamp created_at;
    @Enumerated(value = EnumType.STRING)
    private ExecutionType executionType;
    private BigDecimal priceTarget;
    @OneToMany
    private List <Trades> trades;
    @ManyToOne
    private User user;

}
