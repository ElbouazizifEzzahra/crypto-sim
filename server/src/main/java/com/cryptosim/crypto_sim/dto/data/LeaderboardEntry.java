package com.cryptosim.crypto_sim.dto.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LeaderboardEntry {
    private String username;
    private BigDecimal totalNetWorth;
    private int rank;
}
