package com.example.jobprocessor.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.function.Function;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSignInKey() {

    return Keys.hmacShaKeyFor(
            secret.getBytes()
    );

}
public String generateToken(String username) {

    return Jwts.builder()

            .subject(username)

            .issuedAt(new Date())

            .expiration(
                    new Date(
                            System.currentTimeMillis()
                                    + expiration
                    )
            )

            .signWith(getSignInKey())

            .compact();
}

public String extractUsername(String token) {

    return extractClaim(
            token,
            Claims::getSubject
    );

}
public <T> T extractClaim(
        String token,
        Function<Claims, T> claimsResolver) {
    Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
}
private Claims extractAllClaims(String token) {

    return Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

}
public boolean isTokenValid(String token, String username) {

    String extractedUsername = extractUsername(token);

    return extractedUsername.equals(username)
            && !isTokenExpired(token);

}
public Date extractExpiration(String token) {

    return extractClaim(
            token,
            Claims::getExpiration
    );

}
private boolean isTokenExpired(String token) {

    return extractExpiration(token)
            .before(new Date());

}
}