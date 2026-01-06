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

    // Trouver les transactions par utilisateur
    List<Transaction> findByUser(User user);

    // Trouver les transactions par symbole (BTC, ETH, SOL)
    List<Transaction> findBySymbol(String symbol);

    // Trouver les transactions par type (BUY/SELL)
    List<Transaction> findByType(String type);

    // Trouver les transactions d'un utilisateur par symbole
    List<Transaction> findByUserIdAndSymbol(Long userId, String symbol);

    // Trouver les transactions d'un utilisateur par type
    List<Transaction> findByUserIdAndType(Long userId, String type);

    // Trouver les transactions récentes (dernières 24h)
    @Query("SELECT t FROM Transaction t WHERE t.timestamp >= :since")
    List<Transaction> findRecentTransactions(@Param("since") LocalDateTime since);

    // Trouver les transactions d'un utilisateur avec pagination
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId ORDER BY t.timestamp DESC")
    List<Transaction> findUserTransactionsWithPagination(
            @Param("userId") Long userId,
            org.springframework.data.domain.Pageable pageable);

    // Calculer le volume total d'achat d'un utilisateur pour un symbole
    @Query("SELECT SUM(t.totalAmount) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.symbol = :symbol AND t.type = 'BUY'")
    Double getTotalBuyAmountByUserAndSymbol(@Param("userId") Long userId,
                                            @Param("symbol") String symbol);

    // Calculer le volume total de vente d'un utilisateur pour un symbole
    @Query("SELECT SUM(t.totalAmount) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.symbol = :symbol AND t.type = 'SELL'")
    Double getTotalSellAmountByUserAndSymbol(@Param("userId") Long userId,
                                             @Param("symbol") String symbol);

    // Calculer la quantité totale de crypto achetée
    @Query("SELECT SUM(t.quantity) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.symbol = :symbol AND t.type = 'BUY'")
    Double getTotalQuantityBought(@Param("userId") Long userId,
                                  @Param("symbol") String symbol);

    // Calculer la quantité totale de crypto vendue
    @Query("SELECT SUM(t.quantity) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.symbol = :symbol AND t.type = 'SELL'")
    Double getTotalQuantitySold(@Param("userId") Long userId,
                                @Param("symbol") String symbol);

    // Trouver la dernière transaction d'un utilisateur
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
            "ORDER BY t.timestamp DESC LIMIT 1")
    Transaction findLatestTransactionByUserId(@Param("userId") Long userId);

    // Statistiques pour le dashboard
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.id = :userId")
    Long countTransactionsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.id = :userId AND t.type = 'BUY'")
    Long countBuyTransactionsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.id = :userId AND t.type = 'SELL'")
    Long countSellTransactionsByUserId(@Param("userId") Long userId);
}
