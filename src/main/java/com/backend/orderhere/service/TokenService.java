package com.backend.orderhere.service;

import com.backend.orderhere.filter.JwtUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
  private static final int EXPIRATION_TIME_MINUTES = 30;

  //for test
//    private static final int EXPIRATION_TIME_MINUTES = 1;
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final int TOKEN_LENGTH = 6;
  private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private final Map<String, String> codeToTokenMap = new ConcurrentHashMap<>();


  public String generateCode() {
    StringBuilder code = new StringBuilder(TOKEN_LENGTH);
    Random random = new Random();

    for (int i = 0; i < TOKEN_LENGTH; i++) {
      int index = random.nextInt(CHARACTERS.length());
      code.append(CHARACTERS.charAt(index));
    }

    //generate jwt token and map it to codeToTokenMap
    String jwt = JwtUtil.generateToken(code.toString());
    codeToTokenMap.put(code.toString(), jwt);

    return code.toString();
  }

//    public void generateToken(String code) {
//        String jwt = JwtUtil.generateToken(code);
//        codeToTokenMap.put(code, jwt);
//    }

  public boolean isCodeValid(String code) {
    String token = codeToTokenMap.get(code);
    return JwtUtil.checkExpirationTime(token);
  }
}

