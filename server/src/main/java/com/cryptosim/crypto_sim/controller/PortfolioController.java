
package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.service.portfolio.PortfolioService;
import com.cryptosim.crypto_sim.service.trade.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/{userId}/summary")
    public ResponseEntity<PortfolioService.PortfolioSummary> getSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(portfolioService.getPortfolioSummary(userId));
    }

}
