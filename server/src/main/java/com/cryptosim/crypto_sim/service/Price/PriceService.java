package com.cryptosim.crypto_sim.service.Price;

import java.math.BigDecimal;

public interface PriceService {
    BigDecimal getCurrentPrice(String symbol);
}
