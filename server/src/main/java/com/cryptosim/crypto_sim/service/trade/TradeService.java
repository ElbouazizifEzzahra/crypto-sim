package com.cryptosim.crypto_sim.service.trade;

import java.math.BigDecimal;

public interface TradeService {
    public void buy(Long userId, String symbol, BigDecimal quantity);
    public void sell(Long userId, String symbol, BigDecimal quantity);
}
