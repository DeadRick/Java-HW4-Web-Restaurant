package com.example.restaurantnew.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtils {

    public static Key generateSecretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public static String generateSessionToken(String subject, Key secretKey) {
        return Jwts.builder()
                .setSubject(subject)
                .signWith(secretKey)
                .compact();
    }

    public static Jws<Claims> parseToken(String token, Key secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}
