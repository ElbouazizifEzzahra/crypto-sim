package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.model.Wallet;
import com.cryptosim.crypto_sim.repository.UserRepository;
import com.cryptosim.crypto_sim.service.trade.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class WalletController {
    private final TradeService tradeService;
    private final UserRepository userRepository;
    
    private Long getUserIdFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
    
    @GetMapping("/wallet/{userId}")
    public ResponseEntity<Map<String, Object>> getWalletStatus(@PathVariable Long userId) {
        Wallet wallet = tradeService.getWallet(userId);
        // On renvoie le solde et le Net Worth actuel
        Map<String, Object> response = new HashMap<>();
        response.put("usdBalance", wallet.getUsdBalance());
        response.put("btcBalance", wallet.getBtcBalance());
        response.put("ethBalance", wallet.getEthBalance());
        response.put("solBalance", wallet.getSolBalance());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/wallet")
    public ResponseEntity<Map<String, Object>> getWalletStatus(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        Wallet wallet = tradeService.getWallet(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("usdBalance", wallet.getUsdBalance());
        response.put("btcBalance", wallet.getBtcBalance());
        response.put("ethBalance", wallet.getEthBalance());
        response.put("solBalance", wallet.getSolBalance());
        return ResponseEntity.ok(response);
    }
}
