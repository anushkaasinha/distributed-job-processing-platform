package com.example.jobprocessor.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

}