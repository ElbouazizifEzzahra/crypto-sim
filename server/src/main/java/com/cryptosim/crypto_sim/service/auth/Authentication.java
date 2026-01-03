package com.cryptosim.crypto_sim.service.auth;

import com.cryptosim.crypto_sim.dto.auth.AuthRequest;
import org.springframework.http.ResponseEntity;

public interface Authentication {
    public ResponseEntity<?> login(AuthRequest request);
}
