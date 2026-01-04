package com.cryptosim.crypto_sim.service.wallet;

import java.math.BigDecimal;

public interface WalletService {
    public void updateWalletBalance(Long userId, String currency, BigDecimal amount);
    public void lockWalletBalance(Long userId, String currency, BigDecimal amount);
}
