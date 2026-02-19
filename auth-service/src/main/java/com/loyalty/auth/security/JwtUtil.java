package com.loyalty.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserDetails userDetails) {

        List<String> roles = userDetails.getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList();
 
        return "Bearer "+ Jwts.builder()
                            .setSubject(userDetails.getUsername())
                            .claim("roles", roles)
                            .setIssuedAt(new Date())
                            .setExpiration(
                                    new Date(System.currentTimeMillis() + expiration))
                            .signWith(getKey(), SignatureAlgorithm.HS256)
                            .compact();
    }
}
