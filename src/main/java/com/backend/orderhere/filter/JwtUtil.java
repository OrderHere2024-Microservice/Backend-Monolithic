package com.backend.orderhere.filter;

import com.backend.orderhere.config.StaticConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    public static Integer getUserIdFromToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authorizationHeader.substring("Bearer ".length());
        Claims claims = Jwts.parserBuilder().setSigningKey(StaticConfig.JwtSecretKey.getBytes()).build().parseClaimsJws(token).getBody();
        return claims.get("userId", Integer.class);
    }

}
