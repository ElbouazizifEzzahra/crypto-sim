package com.cryptosim.crypto_sim.service.trade;

import com.cryptosim.crypto_sim.model.Transaction;
import com.cryptosim.crypto_sim.model.User;
import com.cryptosim.crypto_sim.model.Side; // BUY or SELL
import com.cryptosim.crypto_sim.repository.TransactionRepository;
import com.cryptosim.crypto_sim.repository.UserRepository;
import com.cryptosim.crypto_sim.service.Price.PriceService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private PriceService priceService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository; // Pour récupérer l'utilisateur

    // Stockage des orders en attente
    private final Map<String, List<PendingOrder>> pendingOrders = new ConcurrentHashMap<>();

    public enum OrderType {
        LIMIT_BUY,    // Achat si prix <= target
        LIMIT_SELL    // Vente si prix >= target
    }

    @Data
    public static class PendingOrder {
        private String orderId;
        private Long userId;           // ID de l'utilisateur
        private String symbol;
        private OrderType type;
        private BigDecimal targetPrice;
        private BigDecimal quantity;
        private LocalDateTime createdAt;
        private boolean executed = false;
    }

    // 📝 CRÉER UN ORDER
    public String createOrder(Long userId, String symbol, OrderType type,
                              BigDecimal targetPrice, BigDecimal quantity) {

        // Vérifier que l'utilisateur existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Validation
        if (targetPrice.compareTo(BigDecimal.ZERO) <= 0 ||
                quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid price or quantity");
        }

        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);

        PendingOrder order = new PendingOrder();
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setSymbol(symbol);
        order.setType(type);
        order.setTargetPrice(targetPrice);
        order.setQuantity(quantity);
        order.setCreatedAt(LocalDateTime.now());

        pendingOrders.computeIfAbsent(symbol, k -> new CopyOnWriteArrayList<>())
                .add(order);

        System.out.println("✅ Order créé: User#" + userId + " - " + orderId +
                " " + type + " " + quantity + " " + symbol + " @ $" + targetPrice);

        return orderId;
    }

    // ⏰ VÉRIFIER ET EXÉCUTER LES ORDERS
    @Scheduled(fixedDelay = 1000)
    public void checkAndExecuteOrders() {
        for (Map.Entry<String, List<PendingOrder>> entry : pendingOrders.entrySet()) {
            String symbol = entry.getKey();
            List<PendingOrder> orders = entry.getValue();

            BigDecimal currentPrice = priceService.getCurrentPrice(symbol);
            if (currentPrice == null) continue;

            Iterator<PendingOrder> iterator = orders.iterator();
            while (iterator.hasNext()) {
                PendingOrder order = iterator.next();

                if (order.isExecuted()) {
                    iterator.remove();
                    continue;
                }

                boolean shouldExecute = false;

                // Logique d'exécution
                if (order.getType() == OrderType.LIMIT_BUY) {
                    // Achat si prix <= target
                    shouldExecute = currentPrice.compareTo(order.getTargetPrice()) <= 0;
                } else {
                    // Vente si prix >= target
                    shouldExecute = currentPrice.compareTo(order.getTargetPrice()) >= 0;
                }

                if (shouldExecute) {
                    executeOrder(order, currentPrice);
                    order.setExecuted(true);
                    iterator.remove();
                }
            }
        }
    }

    // 🚀 EXÉCUTER UN ORDER
    private void executeOrder(PendingOrder order, BigDecimal executionPrice) {
        try {
            // Récupérer l'utilisateur
            User user = userRepository.findById(order.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            System.out.println("🚀 Exécution order: " + order.getOrderId() +
                    " User#" + order.getUserId() + " " + order.getType() +
                    " " + order.getQuantity() + " " + order.getSymbol() +
                    " @ $" + executionPrice);

            // Créer la transaction
            Transaction transaction = new Transaction();
            transaction.setUser(user);
            transaction.setSymbol(order.getSymbol());
            transaction.setQuantity(order.getQuantity());
            transaction.setPriceAtTransaction(executionPrice);

            // Calculer le montant total
            BigDecimal totalAmount = order.getQuantity().multiply(executionPrice);
            transaction.setTotalAmount(totalAmount);

            // Déterminer le type (BUY/SELL)
            if (order.getType() == OrderType.LIMIT_BUY) {
                transaction.setType(Side.BUY);
            } else {
                transaction.setType(Side.SELL);
            }

            transaction.setTimestamp(LocalDateTime.now());

            // Sauvegarder la transaction
            transactionRepository.save(transaction);

            System.out.println("✅ Transaction sauvegardée: ID " + transaction.getId() +
                    " - Montant: $" + totalAmount);

        } catch (Exception e) {
            System.err.println("❌ Erreur exécution order " + order.getOrderId() +
                    ": " + e.getMessage());
        }
    }

    // 📋 LISTER LES ORDERS EN ATTENTE
    public List<PendingOrder> getPendingOrdersForUser(Long userId) {
        return pendingOrders.values().stream()
                .flatMap(List::stream)
                .filter(order -> order.getUserId().equals(userId) && !order.isExecuted())
                .collect(Collectors.toList());
    }

    // ❌ ANNULER UN ORDER
    public boolean cancelOrder(Long userId, String orderId) {
        for (List<PendingOrder> orders : pendingOrders.values()) {
            for (PendingOrder order : orders) {
                if (order.getOrderId().equals(orderId) &&
                        order.getUserId().equals(userId) &&
                        !order.isExecuted()) {

                    orders.remove(order);
                    System.out.println("❌ Order annulé: User#" + userId + " - " + orderId);
                    return true;
                }
            }
        }
        return false;
    }
}