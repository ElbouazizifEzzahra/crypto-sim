package com.cryptosim.crypto_sim.repository;

import com.cryptosim.crypto_sim.model.Orders;
import com.cryptosim.crypto_sim.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Long, Wallet> {
    Optional<Wallet> findByUserIdAndCurrency(Long userId, String currency);

    // Find all wallets for a user
    List<Wallet> findByUserId(Long userId);
    void save(Wallet wallet);
}