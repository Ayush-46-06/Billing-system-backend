package com.athenura.billing_system.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final String SECRET =
            "thisisaverysecuresecretkeythisisaverysecuresecretkey";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }


    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", List.of("ROLE_" + role))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }


    public List<String> extractRoles(String token) {
        try {
            Claims claims = extractAllClaims(token);

            Object roles = claims.get("roles");

            if (roles instanceof List<?>) {
                return ((List<?>) roles)
                        .stream()
                        .map(Object::toString)
                        .toList();
            }

            return List.of();

        } catch (Exception e) {
            return List.of();
        }
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);

            return !claims.getExpiration().before(new Date());

        } catch (Exception e) {
            return false;
        }
    }
}