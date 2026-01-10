package com.cryptosim.crypto_sim.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Active un broker simple pour envoyer des messages aux clients
        config.enableSimpleBroker("/topic");
        // Préfixe pour les messages envoyés du client vers le serveur
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Point d'entrée pour la connexion React (avec support SockJS)
        // Frontend connects to /ws-crypto, SockJS will handle /info and other sub-paths
        registry.addEndpoint("/ws-crypto")
                .setAllowedOriginPatterns("*")  // Accepte toutes les origines
                .setHandshakeHandler(new DefaultHandshakeHandler())
                .withSockJS();
    }
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(128 * 1024);
        registration.setSendTimeLimit(20 * 1000);
        registration.setSendBufferSizeLimit(512 * 1024);
    }
}