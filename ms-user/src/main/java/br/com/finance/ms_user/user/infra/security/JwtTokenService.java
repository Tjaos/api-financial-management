package br.com.finance.ms_user.user.infra.security;

import br.com.finance.ms_user.user.application.gateway.TokenService;
import br.com.finance.ms_user.user.domain.entities.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenService implements TokenService {

    private final Key key;
    private final long expirationMillis;

    public JwtTokenService(String secret, long expirationMillis) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMillis;
    }


    @Override
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + this.expirationMillis);

        return Jwts.builder()
                .setIssuer("api-financial-management")
                .setSubject(user.getEmail())
                .claim("userId", user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
