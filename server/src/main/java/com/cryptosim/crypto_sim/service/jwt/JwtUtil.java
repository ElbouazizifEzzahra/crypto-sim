package com.cryptosim.crypto_sim.service.jwt;

import com.cryptosim.crypto_sim.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;



import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Component
public class JwtUtil {


    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public String generateToken(User user) {
        String token = Jwts
                .builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + 400 * 60 * 1000))
                .setIssuedAt(new Date())
                .signWith(getSignKey())
                .compact();
        return token;
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractAllClaims(String token) {
        JwtParserBuilder builder = Jwts.parserBuilder().setSigningKey(getSignKey());
        builder.setAllowedClockSkewSeconds(60);
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validiteToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }





}