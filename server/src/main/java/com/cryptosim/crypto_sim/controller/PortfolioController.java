/*
package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.dto.portfolio.LimitOrderRequest;
import com.cryptosim.crypto_sim.model.Transaction;
import com.cryptosim.crypto_sim.repository.TransactionRepository;
import com.cryptosim.crypto_sim.service.trade.OrderService;
import com.cryptosim.crypto_sim.service.portfolio.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin("*")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/{userId}/summary")
    public ResponseEntity<PortfolioService.PortfolioSummary> getPortfolioSummary(@PathVariable String userId) {
        PortfolioService.PortfolioSummary summary = portfolioService.getPortfolioSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<Transaction>> getTransactionHistory(
            @PathVariable String userId,
            @RequestParam(defaultValue = "30") int days) {

        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<Transaction> history = transactionRepository.findByUserIdAndTimestampAfter(userId, since);

        return ResponseEntity.ok(history);
    }

    @PostMapping("/{userId}/orders/limit")
    public ResponseEntity<Map<String, String>> placeLimitOrder(
            @PathVariable String userId,
            @RequestBody LimitOrderRequest request) {

        String orderId = orderService.placeLimitOrder(
                userId,
                request.getSymbol(),
                request.getOrderType(),
                request.getTargetPrice(),
                request.getQuantity()
        );

        return ResponseEntity.ok(Map.of("orderId", orderId, "status", "PENDING"));
    }

    @GetMapping("/{userId}/orders/pending")
    public ResponseEntity<List<OrderService.PendingOrder>> getPendingOrders(@PathVariable String userId) {
        List<OrderService.PendingOrder> orders = orderService.getPendingOrdersForUser(userId);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<Map<String, String>> cancelOrder(
            @PathVariable String userId,
            @PathVariable String orderId) {

        boolean cancelled = orderService.cancelOrder(userId, orderId);
        if (cancelled) {
            return ResponseEntity.ok(Map.of("status", "CANCELLED", "message", "Order cancelled successfully"));
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "ERROR", "message", "Order not found or already executed"));
        }
    }
}*/
