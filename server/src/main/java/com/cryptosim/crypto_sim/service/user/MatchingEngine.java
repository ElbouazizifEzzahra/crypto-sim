package com.cryptosim.crypto_sim.service.user;

import com.cryptosim.crypto_sim.model.*;
import com.cryptosim.crypto_sim.repository.OrdersRepository;
import com.cryptosim.crypto_sim.repository.TradesRepository;
import com.cryptosim.crypto_sim.repository.WalletRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class MatchingEngine {

    private final OrdersRepository orderRepository;
    private final TradesRepository tradeRepository;
    private final WalletRepository walletRepository;

    public MatchingEngine(OrdersRepository orderRepository, TradesRepository tradeRepository, WalletRepository walletRepository) {
        this.orderRepository = orderRepository;
        this.tradeRepository = tradeRepository;
        this.walletRepository = walletRepository;
    }

    // Runs every 1 second to find matches
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void matchOrders() {
        // 1. Fetch all PENDING orders
        // Note: In a real app, you would sort these by Price + Time in the SQL query
        List<Orders> buyOrders = orderRepository.findByStatutOrder(Status.PENDING);
        List<Orders> sellOrders = orderRepository.findByStatutOrder(Status.PENDING);

        // Filter lists manually for simplicity (Real app does this in DB)
        List<Orders> buys = buyOrders.stream().filter(o -> o.getTypeOrder() == Side.BUY).toList();
        List<Orders> sells = sellOrders.stream().filter(o -> o.getTypeOrder() == Side.SELL).toList();

        // 2. The Matching Loop
        for (Orders buy : buys) {
            if (buy.getStatutOrder() != Status.PENDING && buy.getStatutOrder() != Status.PARTIALLY_FILLED) continue;

            for (Orders sell : sells) {
                if (sell.getStatutOrder() != Status.PENDING && sell.getStatutOrder() != Status.PARTIALLY_FILLED) continue;

                // CHECK: Is the Buy Price >= Sell Price?
                if (buy.getPriceTarget().compareTo(sell.getPriceTarget()) >= 0) {

                    // MATCH FOUND! 💥
                    executeTrade(buy, sell);

                    // If buy order is fully filled, stop looking for sellers for this specific buy order
                    if (buy.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                        break;
                    }
                }
            }
        }
    }

    private void executeTrade(Orders buy, Orders sell) {
        // 1. Determine Quantity to Trade (The smallest of the two)
        BigDecimal tradeQty = buy.getQuantity().min(sell.getQuantity());
        BigDecimal tradePrice = sell.getPriceTarget(); // Usually executes at the Seller's price (Maker price)

        System.out.println("✅ MATCH! " + tradeQty + " BTC @ $" + tradePrice);

        // 2. Create the Trade Record
        Trades trade = new Trades();
        trade.setBuyerOrder(buy);
        trade.setSellerOrder(sell);
        trade.setAmount(tradeQty);
        trade.setPrice(tradePrice);
        trade.setCurrentTimestamp(Timestamp.from(Instant.now()));
        tradeRepository.save(trade);

        // 3. Update Order Quantities (Reduce what is left to buy/sell)
        buy.setQuantity(buy.getQuantity().subtract(tradeQty));
        sell.setQuantity(sell.getQuantity().subtract(tradeQty));

        // 4. Update Status
        updateOrderStatus(buy);
        updateOrderStatus(sell);

        // 5. SETTLEMENT: Move the Money! 💰
        settleWallets(buy.getUser(), sell.getUser(), tradeQty, tradePrice);
    }

    private void updateOrderStatus(Orders order) {
        if (order.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            order.setStatutOrder(Status.FILLED);
        } else {
            order.setStatutOrder(Status.PARTIALLY_FILLED);
        }
        orderRepository.save(order);
    }

    private void settleWallets(User buyer, User seller, BigDecimal qty, BigDecimal price) {
        // Buyer: -USD (Locked), +BTC (Balance)
        // Seller: -BTC (Locked), +USD (Balance)
        BigDecimal totalCost = price.multiply(qty);

        // BUYER UPDATES
        Wallet buyerUsd = walletRepository.findByUserIdAndCurrency(buyer.getId(), "USD").orElseThrow();
        Wallet buyerBtc = walletRepository.findByUserIdAndCurrency(buyer.getId(), "BTC").orElseThrow();

        // Unlock the USD and remove it (It's paid to seller)
        buyerUsd.setLocked(buyerUsd.getLocked().subtract(totalCost));
        buyerUsd.setBalance(buyerUsd.getBalance().subtract(totalCost));
        // Give BTC
        buyerBtc.setBalance(buyerBtc.getBalance().add(qty));

        // SELLER UPDATES
        Wallet sellerUsd = walletRepository.findByUserIdAndCurrency(seller.getId(), "USD").orElseThrow();
        Wallet sellerBtc = walletRepository.findByUserIdAndCurrency(seller.getId(), "BTC").orElseThrow();

        // Unlock the BTC and remove it (It's given to buyer)
        sellerBtc.setLocked(sellerBtc.getLocked().subtract(qty));
        sellerBtc.setBalance(sellerBtc.getBalance().subtract(qty));
        // Give USD
        sellerUsd.setBalance(sellerUsd.getBalance().add(totalCost));

        // Save all changes
        walletRepository.saveAll(List.of(buyerUsd, buyerBtc, sellerUsd, sellerBtc));
    }
}
