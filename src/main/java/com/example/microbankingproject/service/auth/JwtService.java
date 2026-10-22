package com.example.microbankingproject.service.auth;

import com.example.microbankingproject.entity.enums.TokenType;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public interface JwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails, TokenType tokenType);

    String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            TokenType tokenType
    );

    Boolean isTokenValid(String token, UserDetails userDetails);

    Claims extractAllClaims(String token);

    List<SimpleGrantedAuthority> extractRoles(String token);
}

