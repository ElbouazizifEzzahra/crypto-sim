/*
package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.dto.trade.TradeDtoRequest;
import com.cryptosim.crypto_sim.service.trade.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trader")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/buy")
    public ResponseEntity<?> buyCrypto(@RequestBody TradeDtoRequest request) {
        tradeService.buy(request.getUserId(), request.getSymbol(), request.getQuantity());
        return ResponseEntity.ok("Achat réussi");
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellCrypto(@RequestBody TradeDtoRequest request) {
        tradeService.sell(request.getUserId(), request.getSymbol(), request.getQuantity());
        return ResponseEntity.ok("Vente réussie");
    }
}
*/
