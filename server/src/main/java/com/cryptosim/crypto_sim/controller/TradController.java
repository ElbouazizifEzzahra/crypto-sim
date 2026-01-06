package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.model.Transaction;
import com.cryptosim.crypto_sim.model.Wallet;
import com.cryptosim.crypto_sim.service.trade.TradeService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradController {
    private final TradeService tradeService;

    @PostMapping("/buy")
    public ResponseEntity<?> buyCrypto(
            @RequestBody BuyRequest request,
            @RequestHeader("X-User-ID") Long userId) {
        try {
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
            @RequestHeader("X-User-ID") Long userId) {
        try {
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
}
