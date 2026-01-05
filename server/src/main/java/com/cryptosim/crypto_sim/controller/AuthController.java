package com.cryptosim.crypto_sim.controller;

import com.cryptosim.crypto_sim.dto.auth.AuthRequest;
import com.cryptosim.crypto_sim.service.auth.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class AuthController {
    private final Authentication authentication;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        return authentication.login(authRequest);

    }

}