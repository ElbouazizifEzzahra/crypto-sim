package com.cryptosim.crypto_sim.repository;

import com.cryptosim.crypto_sim.model.User;
import com.cryptosim.crypto_sim.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findByUserId(Long userId);


    // OU si tu n'utilises pas "currency" dans Wallet, utilise :
    Optional<Wallet> findByUser_Id(Long userId);


    // Trouver le wallet par User
    Optional<Wallet> findByUser(User user);

    // Trouver tous les wallets pour le leaderboard
    @Query("SELECT w FROM Wallet w ORDER BY w.usdBalance DESC")
    List<Wallet> findAllOrderByUsdBalanceDesc();

    // Mettre à jour le solde USD
    @Query("UPDATE Wallet w SET w.usdBalance = :newBalance WHERE w.id = :walletId")
    void updateUsdBalance(@Param("walletId") Long walletId,
                          @Param("newBalance") BigDecimal newBalance);

    // Vérifier si un utilisateur a assez d'USD
    @Query("SELECT CASE WHEN w.usdBalance >= :amount THEN true ELSE false END " +
            "FROM Wallet w WHERE w.user.id = :userId")
    boolean hasSufficientUsd(@Param("userId") Long userId,
                             @Param("amount") BigDecimal amount);

    // Trouver les wallets avec solde minimum
    @Query("SELECT w FROM Wallet w WHERE w.usdBalance >= :minBalance")
    List<Wallet> findByMinUsdBalance(@Param("minBalance") BigDecimal minBalance);


}