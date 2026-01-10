package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.dto.LimitOrderRequest;
import com.cryptosim.crypto_sim.model.Transaction;
import com.cryptosim.crypto_sim.repository.TransactionRepository;
import com.cryptosim.crypto_sim.service.trade.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TransactionRepository transactionRepository;

    // ➕ CRÉER UN ORDER
    @PostMapping("/users/{userId}/create")
    public ResponseEntity<Map<String, String>> createOrder(
            @PathVariable Long userId,
            @RequestBody LimitOrderRequest request) {

        try {
            String orderId = orderService.createOrder(
                    userId,
                    request.getSymbol(),
                    request.getOrderType(),
                    request.getTargetPrice(),
                    request.getQuantity()
            );

            return ResponseEntity.ok(Map.of(
                    "orderId", orderId,
                    "status", "PENDING",
                    "message", "Order created successfully"
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    // 📋 ORDERS EN ATTENTE
    @GetMapping("/users/{userId}/pending")
    public ResponseEntity<List<OrderService.PendingOrder>> getPendingOrders(
            @PathVariable Long userId) {

        return ResponseEntity.ok(orderService.getPendingOrdersForUser(userId));
    }

    // ❌ ANNULER UN ORDER
    @DeleteMapping("/users/{userId}/cancel/{orderId}")
    public ResponseEntity<Map<String, String>> cancelOrder(
            @PathVariable Long userId,
            @PathVariable String orderId) {

        boolean cancelled = orderService.cancelOrder(userId, orderId);

        if (cancelled) {
            return ResponseEntity.ok(Map.of(
                    "status", "CANCELLED",
                    "message", "Order cancelled successfully"
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Order not found or already executed"
            ));
        }
    }

    // 📜 HISTORIQUE DES TRANSACTIONS
    @GetMapping("/users/{userId}/history")
    public ResponseEntity<List<Transaction>> getTransactionHistory(
            @PathVariable Long userId,
            @RequestParam(required = false) String symbol) {

        List<Transaction> transactions;

        if (symbol != null && !symbol.isEmpty()) {
            // Filtrer par symbole
            transactions = transactionRepository.findByUserIdAndSymbol(userId, symbol);
        } else {
            // Toutes les transactions de l'utilisateur
            transactions = transactionRepository.findByUserId(userId);
        }

        return ResponseEntity.ok(transactions);
    }
}