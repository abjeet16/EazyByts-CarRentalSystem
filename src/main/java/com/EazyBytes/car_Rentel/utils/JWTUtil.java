package com.EazyBytes.car_Rentel.utils;

import com.EazyBytes.car_Rentel.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {

    // Extracts the username from the JWT token
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // Generates a new JWT token using the user details
    public String generateToken(UserDetails user){
        return generateToken(new HashMap<>(), user);
    }

    // Checks if the provided JWT token is valid for the given user
    public boolean isTokenValid(String token, UserDetails user){
        final String userName = extractUserName(token);
        return (userName.equals(user.getUsername()) && !isTokenExpired(token));
    }

    // Extracts a specific claim from the JWT token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Generates a JWT token with extra claims and user details
    private String generateToken(Map<String, Object> extraClaims, UserDetails user){
        return Jwts.builder()
                .setClaims(extraClaims) // Add extra claims
                .setSubject(user.getUsername())// Set the username
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set the issued date
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // Set the expiration time (24 hours)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign the token with the secret key
                .compact();
    }

    // Generates a refresh token with extra claims and user details
    public String generateRefreshToken(Map<String, Object> extraClaims,UserDetails user){
        return Jwts.builder()
                .setClaims(extraClaims) // Add extra claims
                .setSubject(user.getUsername()) // Set the username
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set the issued date
                .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // Set the expiration time (7 days)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign the token with the secret key
                .compact();
    }

    // Checks if the JWT token has expired
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // Extracts the expiration date from the JWT token
    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    // Extracts all claims (information) from the JWT token
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Set the signing key to verify the token
                .build()
                .parseClaimsJws(token) // Parse the token to get the claims
                .getBody();
    }

    // Provides the secret key used to sign the JWT tokens
    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode("413F4428472B4B6250655368566D5970337336763979244226452948404D6351");
        return Keys.hmacShaKeyFor(keyBytes); // Generate the key for HMAC signing
    }
}

