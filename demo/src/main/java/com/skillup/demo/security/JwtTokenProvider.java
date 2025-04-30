package com.skillup.demo.security;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

import com.skillup.demo.config.SecurityContest;
import com.skillup.demo.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    public JwtTokenClaims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SecurityContest.JWT_KEY.getBytes());
        
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        String username = String.valueOf(claims.get("username"));

        JwtTokenClaims jwtTokenClaims = new JwtTokenClaims();
        jwtTokenClaims.setUsername(username);

        return jwtTokenClaims;
    }
    
    public String generateJwtToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(SecurityContest.JWT_KEY.getBytes());
        
        String jwt = Jwts.builder()
                .setIssuer("Ashok Zarmariya")
                .claim("username", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 990000000))
                .signWith(key).compact();
        
        return jwt;
    }
}