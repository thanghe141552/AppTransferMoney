package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtUtils {
    private static final SecretKey SECRET = generateKey();

    public static String generateToken(String id, String roles) {
        return Jwts.builder()
                .subject(id)
                .claim("role", roles)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(SECRET)
                .compact();
    }

    public static String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(SECRET)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET)
                .build()
                .parseSignedClaims(token).getPayload();
    }


    private static SecretKey generateKey() {
        byte[] key = new byte[32];
        new java.util.Random().nextBytes(key);
        return Keys.hmacShaKeyFor(key);
    }
}
