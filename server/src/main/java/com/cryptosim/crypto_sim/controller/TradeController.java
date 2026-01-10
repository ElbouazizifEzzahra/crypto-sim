package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.model.Transaction;
import com.cryptosim.crypto_sim.model.Wallet;
import com.cryptosim.crypto_sim.repository.TransactionRepository;
import com.cryptosim.crypto_sim.repository.UserRepository;
import com.cryptosim.crypto_sim.service.trade.TradeService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    
    private Long getUserIdFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buyCrypto(
            @RequestBody BuyRequest request,
            Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            Transaction transaction = tradeService.buyCrypto(
                    userId,
                    request.getSymbol(),
                    request.getQuantity()
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Achat effectué avec succès",
                    "transaction", transaction
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellCrypto(
            @RequestBody SellRequest request,
            Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            Transaction transaction = tradeService.sellCrypto(
                    userId,
                    request.getSymbol(),
                    request.getQuantity()
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Vente effectuée avec succès",
                    "transaction", transaction
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
    
    @PostMapping("/execute")
    public ResponseEntity<?> executeTrade(
            @RequestBody ExecuteRequest request,
            Authentication authentication) {
        try {
            // Validate request
            if (request == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Request body is required"
                ));
            }
            
            if (request.getType() == null || request.getType().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Trade type (BUY/SELL) is required"
                ));
            }
            
            if (request.getSymbol() == null || request.getSymbol().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Symbol is required"
                ));
            }
            
            if (request.getQuantity() == null || request.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Quantity must be greater than 0"
                ));
            }
            
            Long userId = getUserIdFromAuthentication(authentication);
            Transaction transaction;
            
            if ("BUY".equalsIgnoreCase(request.getType())) {
                transaction = tradeService.buyCrypto(
                        userId,
                        request.getSymbol(),
                        request.getQuantity()
                );
            } else if ("SELL".equalsIgnoreCase(request.getType())) {
                transaction = tradeService.sellCrypto(
                        userId,
                        request.getSymbol(),
                        request.getQuantity()
                );
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Invalid trade type. Use BUY or SELL"
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Trade executed successfully",
                    "transaction", transaction
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage() != null ? e.getMessage() : "Trade execution failed"
            ));
        }
    }

    @GetMapping("/portfolio/{userId}")
    public ResponseEntity<?> getPortfolio(@PathVariable Long userId) {
        try {
            // Récupérer portefeuille + calculer valeurs
            Wallet wallet = tradeService.getWallet(userId);
            Map<String, Object> portfolio = new HashMap<>();

            portfolio.put("usdBalance", wallet.getUsdBalance());
            portfolio.put("btc", Map.of(
                    "quantity", wallet.getBtcBalance(),
                    "value", tradeService.calculateCryptoValue("BTC", wallet.getBtcBalance())
            ));
            portfolio.put("eth", Map.of(
                    "quantity", wallet.getEthBalance(),
                    "value", tradeService.calculateCryptoValue("ETH", wallet.getEthBalance())
            ));
            portfolio.put("sol", Map.of(
                    "quantity", wallet.getSolBalance(),
                    "value", tradeService.calculateCryptoValue("SOL", wallet.getSolBalance())
            ));

            return ResponseEntity.ok(portfolio);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/portfolio")
    public ResponseEntity<?> getPortfolio(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            Wallet wallet = tradeService.getWallet(userId);
            Map<String, Object> portfolio = new HashMap<>();
            
            // Format for frontend
            portfolio.put("balance", wallet.getUsdBalance().doubleValue());
            
            // Assets array
            List<Map<String, Object>> assets = new java.util.ArrayList<>();
            
            if (wallet.getBtcBalance().compareTo(BigDecimal.ZERO) > 0) {
                assets.add(Map.of(
                    "symbol", "BTC",
                    "quantity", wallet.getBtcBalance().doubleValue()
                ));
            }
            if (wallet.getEthBalance().compareTo(BigDecimal.ZERO) > 0) {
                assets.add(Map.of(
                    "symbol", "ETH",
                    "quantity", wallet.getEthBalance().doubleValue()
                ));
            }
            if (wallet.getSolBalance().compareTo(BigDecimal.ZERO) > 0) {
                assets.add(Map.of(
                    "symbol", "SOL",
                    "quantity", wallet.getSolBalance().doubleValue()
                ));
            }
            
            portfolio.put("assets", assets);

            return ResponseEntity.ok(portfolio);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            List<Transaction> transactions = transactionRepository.findByUserId(userId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<TradeService.LeaderboardEntry>> getLeaderboard() {
        return ResponseEntity.ok(tradeService.getGlobalLeaderboard());
    }

    @Data
    public static class BuyRequest {
        private String symbol;
        private BigDecimal quantity;
    }

    @Data
    public static class SellRequest {
        private String symbol;
        private BigDecimal quantity;
    }
    
    @Data
    public static class ExecuteRequest {
        private String symbol;
        private BigDecimal quantity;
        private String type; // "BUY" or "SELL"
        private BigDecimal price; // Optional, for record keeping
    }
}
