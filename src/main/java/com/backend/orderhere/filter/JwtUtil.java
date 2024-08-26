package com.backend.orderhere.filter;

import com.backend.orderhere.auth.ApplicationUserDetails;
import com.backend.orderhere.config.StaticConfig;
import com.backend.orderhere.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    //generate Jwt token based on Authentication(username and password)
    public static String generateToken(Authentication authResult) {
        ApplicationUserDetails applicationUserDetails = (ApplicationUserDetails) authResult.getPrincipal();
        return Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .claim("userId", applicationUserDetails.getUserId())
                .claim("avatarURL", applicationUserDetails.getUserAvatarURL())
                .claim("userName", applicationUserDetails.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusMillis(24 * 60 * 60 * 1000))) //24 hours expiration
                .signWith(Keys.hmacShaKeyFor(StaticConfig.JwtSecretKey.getBytes()))
                .compact();
    }

    //generate Jwt token based on Exist user
    public static String generateToken(User user) {

        Map<String, String> roleMap = new HashMap<>();
        roleMap.put("authority", "ROLE_" + user.getUserRole().toString());
        List<Map<String, String>> authorities = new ArrayList<>();
        authorities.add(roleMap);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("authorities", authorities)
                .claim("userId", user.getUserId())
                .claim("avatarURL", user.getAvatarUrl())
                .claim("userName", user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusMillis(24 * 60 * 60 * 1000))) // 24 hours expiration
                .signWith(Keys.hmacShaKeyFor(StaticConfig.JwtSecretKey.getBytes()))
                .compact();
    }


    //generate Jwt token based on Generated code for reset password
    public static String generateToken(String code) {
        return Jwts.builder()
                .setSubject(code)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusMillis(24 * 60 * 60 * 1000))) //24 hours expiration
                .signWith(Keys.hmacShaKeyFor(StaticConfig.JwtSecretKey.getBytes()))
                .compact();
    }

    //verify Jwt expiration
    public static boolean checkExpirationTime(String jwtToken) {
        if (jwtToken == null) {
            return false;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(StaticConfig.JwtSecretKey.getBytes()))
                    .build()
                    .parseClaimsJws(jwtToken);

            //check Expiration
            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static Integer getUserIdFromToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authorizationHeader.substring("Bearer ".length());
        Claims claims = Jwts.parserBuilder().setSigningKey(StaticConfig.JwtSecretKey.getBytes()).build().parseClaimsJws(token).getBody();
        return claims.get("userId", Integer.class);
    }

}
