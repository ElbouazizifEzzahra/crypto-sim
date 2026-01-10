
package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.model.Wallet;
import com.cryptosim.crypto_sim.repository.UserRepository;
import com.cryptosim.crypto_sim.service.portfolio.PortfolioService;
import com.cryptosim.crypto_sim.service.trade.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;
    
    private final UserRepository userRepository;
    private final TradeService tradeService;
    
    private Long getUserIdFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @GetMapping("/{userId}/summary")
    public ResponseEntity<PortfolioService.PortfolioSummary> getSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(portfolioService.getPortfolioSummary(userId));
    }
    
    @GetMapping
    public ResponseEntity<?> getPortfolio(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            Wallet wallet = tradeService.getWallet(userId);
            Map<String, Object> portfolio = new HashMap<>();
            
            portfolio.put("balance", wallet.getUsdBalance().doubleValue());
            
            List<Map<String, Object>> assets = new ArrayList<>();
            
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

}
