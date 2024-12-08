package com.backend.orderhere.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtUtil {

    private PublicKey publicKey;

    @Value("${keyCloak.jwt.publicKey}")
    public void setPublicKey(String publicKeyStr) {
        try {

            byte[] decodedKey = Base64.getDecoder().decode(publicKeyStr);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            this.publicKey = factory.generatePublic(spec);
        } catch (Exception e) {
            log.error("Failed to parse public key: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid public key provided");
        }
    }

    public String getUserIdFromToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("Authorization header is missing or does not contain a Bearer token");
            return null;
        }
        String token = authorizationHeader.substring("Bearer ".length());

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            log.error("Failed to parse token: {}", e.getMessage());
            return null;
        }
    }
}
