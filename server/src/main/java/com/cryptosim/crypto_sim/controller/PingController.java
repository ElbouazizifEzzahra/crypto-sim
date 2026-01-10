package com.cryptosim.crypto_sim.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api") // Crucial: Matches your Nginx location block
public class PingController {

    // Test 1: Simple connectivity check
    // URL: http://localhost/api/ping
    @GetMapping("/ping")
    public String ping() {
        return "Pong! Backend is running. Time: " + LocalDateTime.now();
    }

    // Test 2: Debugging info (JSON format)
    // URL: http://localhost/api/info
    @GetMapping("/info")
    public Map<String, String> getSystemInfo(HttpServletRequest request) {
        Map<String, String> info = new HashMap<>();
        
        info.put("status", "UP");
        info.put("message", "Gateway successfully reached Spring Boot");
        info.put("client_ip", request.getRemoteAddr()); // Shows the IP of the Gateway (Nginx)
        info.put("server_port", String.valueOf(request.getLocalPort()));
        
        return info;
    }
}