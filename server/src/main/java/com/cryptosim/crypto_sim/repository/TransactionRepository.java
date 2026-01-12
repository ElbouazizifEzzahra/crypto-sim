package com.cryptosim.crypto_sim.repository;

import com.cryptosim.crypto_sim.model.Transaction;
import com.cryptosim.crypto_sim.model.Status;
import com.cryptosim.crypto_sim.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {


    // Trouver toutes les transactions d'un utilisateur
    List<Transaction> findByUserId(Long userId);

    // Trouver les transactions d'un utilisateur par symbole
    List<Transaction> findByUserIdAndSymbol(Long userId, String symbol);

}
