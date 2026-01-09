/*
package com.cryptosim.crypto_sim.service.wallet;

import com.cryptosim.crypto_sim.model.Wallet;
import com.cryptosim.crypto_sim.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletUpdateService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private WalletRepository walletRepository;

    */
/**
     * Envoie le nouveau solde à un utilisateur spécifique
     *//*

    public void sendPrivateWalletUpdate(Long userId) {
        // 1. Récupérer les nouvelles données en BDD
        List<Wallet> updatedWallets = walletRepository.findByUserId(userId);

        // 2. Envoyer au canal privé de l'utilisateur
        // Le frontend s'abonne à /user/queue/wallet
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/wallet",
                updatedWallets
        );

        System.out.println("🔔 Notification de solde envoyée à l'utilisateur : " + userId);
    }
}
*/
