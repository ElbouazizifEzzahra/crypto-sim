package com.cryptosim.crypto_sim.service.auth;

import com.cryptosim.crypto_sim.dto.auth.AuthRequest;
import com.cryptosim.crypto_sim.model.User;
import com.cryptosim.crypto_sim.repository.UserRepository;
import com.cryptosim.crypto_sim.service.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class AuthenticationImp implements Authentication{
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;



    public ResponseEntity<?> login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            String token = jwtUtil.generateToken(user);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);


            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("ERREUR lors de l'authentification: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou mot de passe incorrect");
        }
    }

}
