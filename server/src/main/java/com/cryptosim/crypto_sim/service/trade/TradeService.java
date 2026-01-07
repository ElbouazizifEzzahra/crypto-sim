package com.cryptosim.crypto_sim.service.trade;

import com.cryptosim.crypto_sim.model.Side;
import com.cryptosim.crypto_sim.model.Transaction;
import com.cryptosim.crypto_sim.model.User;
import com.cryptosim.crypto_sim.model.Wallet;
import com.cryptosim.crypto_sim.repository.TransactionRepository;
import com.cryptosim.crypto_sim.repository.UserRepository;
import com.cryptosim.crypto_sim.repository.WalletRepository;
import com.cryptosim.crypto_sim.service.Price.PriceService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TradeService {

    private final PriceService priceService;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // AJOUTÉ : Méthode pour récupérer le wallet
    public Wallet getWallet(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Portefeuille non trouvé"));
    }

    // AJOUTÉ : Méthode pour calculer la valeur d'une crypto
    public BigDecimal calculateCryptoValue(String symbol, BigDecimal quantity) {
        return priceService.calculateCryptoValue(symbol, quantity);
    }

    public Transaction buyCrypto(Long userId, String symbol, BigDecimal quantity) {
        BigDecimal currentPrice = priceService.getCurrentPrice(symbol);

        if (currentPrice.compareTo(BigDecimal.ZERO) == 0) {
            throw new RuntimeException("Prix non disponible pour " + symbol);
        }

        BigDecimal totalCost = currentPrice.multiply(quantity);
        Wallet wallet = getWallet(userId);

        if (wallet.getUsdBalance().compareTo(totalCost) < 0) {
            throw new RuntimeException("Solde insuffisant. Nécessaire: $" +
                    totalCost + ", Disponible: $" + wallet.getUsdBalance());
        }

        wallet.setUsdBalance(wallet.getUsdBalance().subtract(totalCost));

        switch (symbol.toUpperCase()) {
            case "BTC":
                wallet.setBtcBalance(wallet.getBtcBalance().add(quantity));
                break;
            case "ETH":
                wallet.setEthBalance(wallet.getEthBalance().add(quantity));
                break;
            case "SOL":
                wallet.setSolBalance(wallet.getSolBalance().add(quantity));
                break;
            default:
                throw new RuntimeException("Cryptomonnaie non supportée: " + symbol);
        }

        walletRepository.save(wallet);

        Transaction transaction = new Transaction();
        transaction.setUser(userRepository.findById(userId).orElseThrow());
        transaction.setType(Side.BUY);
        transaction.setSymbol(symbol);
        transaction.setQuantity(quantity);
        transaction.setPriceAtTransaction(currentPrice);
        transaction.setTotalAmount(totalCost);
        transaction.setTimestamp(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        notifyWalletUpdate(userId);
        notifyTransaction(userId, savedTransaction);

        log.info("BUY - User {} bought {} {} at ${}", userId, quantity, symbol, currentPrice);
        return savedTransaction;
    }

    public Transaction sellCrypto(Long userId, String symbol, BigDecimal quantity) {
        BigDecimal currentPrice = priceService.getCurrentPrice(symbol);
        Wallet wallet = getWallet(userId);

        BigDecimal cryptoBalance = getCryptoBalance(wallet, symbol);
        if (cryptoBalance.compareTo(quantity) < 0) {
            throw new RuntimeException("Quantité insuffisante. Disponible: " +
                    cryptoBalance + " " + symbol + ", Demandé: " + quantity);
        }

        BigDecimal totalAmount = currentPrice.multiply(quantity);
        wallet.setUsdBalance(wallet.getUsdBalance().add(totalAmount));

        switch (symbol.toUpperCase()) {
            case "BTC":
                wallet.setBtcBalance(wallet.getBtcBalance().subtract(quantity));
                break;
            case "ETH":
                wallet.setEthBalance(wallet.getEthBalance().subtract(quantity));
                break;
            case "SOL":
                wallet.setSolBalance(wallet.getSolBalance().subtract(quantity));
                break;
        }

        walletRepository.save(wallet);

        Transaction transaction = new Transaction();
        transaction.setUser(userRepository.findById(userId).orElseThrow());
        transaction.setType(Side.SELL);
        transaction.setSymbol(symbol);
        transaction.setQuantity(quantity);
        transaction.setPriceAtTransaction(currentPrice);
        transaction.setTotalAmount(totalAmount);
        transaction.setTimestamp(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        notifyWalletUpdate(userId);
        notifyTransaction(userId, savedTransaction);

        log.info("SELL - User {} sold {} {} at ${}", userId, quantity, symbol, currentPrice);
        return savedTransaction;
    }

    private BigDecimal getCryptoBalance(Wallet wallet, String symbol) {
        return switch (symbol.toUpperCase()) {
            case "BTC" -> wallet.getBtcBalance();
            case "ETH" -> wallet.getEthBalance();
            case "SOL" -> wallet.getSolBalance();
            default -> BigDecimal.ZERO;
        };
    }

    // AJOUTÉ : Méthode calculateTotalValue dans Wallet
    private BigDecimal calculateWalletTotalValue(Wallet wallet) {
        BigDecimal total = wallet.getUsdBalance();
        total = total.add(priceService.calculateCryptoValue("BTC", wallet.getBtcBalance()));
        total = total.add(priceService.calculateCryptoValue("ETH", wallet.getEthBalance()));
        total = total.add(priceService.calculateCryptoValue("SOL", wallet.getSolBalance()));
        return total;
    }

    private void notifyWalletUpdate(Long userId) {
        Wallet wallet = getWallet(userId);
        BigDecimal netWorth = calculateWalletTotalValue(wallet);

        Map<String, Object> walletUpdate = new HashMap<>();
        walletUpdate.put("wallet", wallet);
        walletUpdate.put("netWorth", netWorth);
        walletUpdate.put("timestamp", LocalDateTime.now());

        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/wallet-update",
                walletUpdate
        );
    }

    private void notifyTransaction(Long userId, Transaction transaction) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/transactions",
                transaction
        );
    }

    public List<LeaderboardEntry> getGlobalLeaderboard() {
        List<Wallet> allWallets = walletRepository.findAll();

        return allWallets.stream()
                .map(wallet -> {
                    BigDecimal netWorth = calculateWalletTotalValue(wallet);
                    // CORRIGÉ : getUsername() au lieu de getLastName()
                    User user = wallet.getUser();
                    String username = (user.getLastName() != null) ? user.getLastName() :
                            (user.getLastName() != null) ? user.getLastName() : "User";

                    return new LeaderboardEntry(
                            username,
                            netWorth,
                            wallet.getUsdBalance(),
                            wallet.getBtcBalance(),
                            wallet.getEthBalance(),
                            wallet.getSolBalance()
                    );
                })
                .sorted((a, b) -> b.getNetWorth().compareTo(a.getNetWorth()))
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    public static class LeaderboardEntry {
        private String username;
        private BigDecimal netWorth;
        private BigDecimal usdBalance;
        private BigDecimal btcBalance;
        private BigDecimal ethBalance;
        private BigDecimal solBalance;
    }
}