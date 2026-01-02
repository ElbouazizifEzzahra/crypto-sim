package com.cryptosim.crypto_sim.repository;

import com.cryptosim.crypto_sim.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Long, Wallet> {

}