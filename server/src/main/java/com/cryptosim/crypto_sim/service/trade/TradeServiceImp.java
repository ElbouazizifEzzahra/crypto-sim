package com.cryptosim.crypto_sim.service.trade;

import com.cryptosim.crypto_sim.model.Orders;
import com.cryptosim.crypto_sim.model.Side;
import com.cryptosim.crypto_sim.model.Wallet;
import com.cryptosim.crypto_sim.repository.OrdersRepository;
import com.cryptosim.crypto_sim.repository.WalletRepository;
import com.cryptosim.crypto_sim.service.Price.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.lang.Long;

@Service
@Transactional
public class TradeServiceImp implements  TradeService{

    @Autowired
    private PriceService priceService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    public void buy(Long userId, String symbol, BigDecimal quantity) {
        // 1. Récupérer le prix en temps réel depuis la Map en RAM (Source of Truth)
        BigDecimal currentPrice = priceService.getCurrentPrice(symbol);
        BigDecimal totalCost = currentPrice.multiply(quantity);

        // 2. Vérifier le solde USD
        Wallet usdWallet = walletRepository.findByUserIdAndCurrency(userId, "USD")
                .orElseThrow(() -> new RuntimeException("USD Wallet non trouvé"));

        if (usdWallet.getBalance().compareTo(totalCost) < 0) {
            throw new RuntimeException("Solde insuffisant pour cet achat");
        }

        // 3. Déduire l'USD et ajouter la Crypto
        usdWallet.setBalance(usdWallet.getBalance().subtract(totalCost));

        Wallet cryptoWallet = walletRepository.findByUserIdAndCurrency(userId, symbol)
                .orElseGet(() -> new Wallet(null, symbol, BigDecimal.ZERO, BigDecimal.ZERO, usdWallet.getUser()));

        cryptoWallet.setBalance(cryptoWallet.getBalance().add(quantity));

        // 4. Sauvegarder les modifications et créer l'ordre (Transaction)
        walletRepository.save(usdWallet);
        walletRepository.save(cryptoWallet);

        Orders order = new Orders();
        order.setUser(usdWallet.getUser());
        order.setTypeOrder(Side.BUY);
        order.setQuantity(quantity);
        order.setPriceTarget(currentPrice);
        order.setCreated_at(new Timestamp(System.currentTimeMillis()));
        ordersRepository.save(order);
    }

    public void sell(Long userId, String symbol, BigDecimal quantity) {
        BigDecimal currentPrice = priceService.getCurrentPrice(symbol);

        Wallet cryptoWallet = walletRepository.findByUserIdAndCurrency(userId, symbol)
                .orElseThrow(() -> new RuntimeException("Vous ne possédez pas cette crypto"));

        if (cryptoWallet.getBalance().compareTo(quantity) < 0) {
            throw new RuntimeException("Quantité de crypto insuffisante");
        }

        // Logique inverse : Déduire Crypto, Ajouter USD
        BigDecimal totalGain = currentPrice.multiply(quantity);
        cryptoWallet.setBalance(cryptoWallet.getBalance().subtract(quantity));

        Wallet usdWallet = walletRepository.findByUserIdAndCurrency(userId, "USD").get();
        usdWallet.setBalance(usdWallet.getBalance().add(totalGain));

        walletRepository.save(cryptoWallet);
        walletRepository.save(usdWallet);

        // Créer l'enregistrement de vente
        Orders order = new Orders();
        order.setTypeOrder(Side.SELL);
        order.setQuantity(quantity);
        order.setPriceTarget(currentPrice);
        order.setCreated_at(new Timestamp(System.currentTimeMillis()));
        ordersRepository.save(order);
    }
}
