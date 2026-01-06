package com.cryptosim.crypto_sim.service.wallet;

import com.cryptosim.crypto_sim.model.Wallet;
import com.cryptosim.crypto_sim.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/*
@Service
@RequiredArgsConstructor
public class WalletServiceImp implements WalletService{
    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public void updateWalletBalance(Long userId, String currency, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserIdAndCurrency(userId, currency)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }
    @Override
    @Transactional
    public void lockWalletBalance(Long userId, String currency, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserIdAndCurrency(userId, currency)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        wallet.setLocked(wallet.getLocked().add(amount));
        walletRepository.save(wallet);
    }
}
*/
