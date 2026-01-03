package com.cryptosim.crypto_sim.dto.trade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeDtoRequest {


    private Long userId;
    private String symbol;
    private BigDecimal quantity;
}
