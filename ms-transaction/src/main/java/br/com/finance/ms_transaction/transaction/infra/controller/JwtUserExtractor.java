package br.com.finance.ms_transaction.transaction.infra.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class JwtUserExtractor {

    private final String secret;

    public JwtUserExtractor(String secret) {
        this.secret = secret;
    }

    public UUID extractUserId(String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "").trim();

        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userId = claims.get("userId", String.class);

        if(userId == null || userId.isBlank()){
            throw new RuntimeException("JWT sem Claim de userId");
        }

        return UUID.fromString(userId);
    }
}
