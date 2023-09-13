package com.example.propertymanagement.service.implementation;

import com.example.propertymanagement.model.UserRole;
import com.example.propertymanagement.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final Logger logger;
    private static final String SECRET_KEY =
            "2646294A404E635266546A576E5A7234753778214125442A472D4B6150645367";
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails) {
        long issuedAt = System.currentTimeMillis();
        long expiresAtAdmin = issuedAt + 1000 * 60 * 60 * 5;
        long expiresAtUser = issuedAt + 1000 * 60 * 25;
        long expiresAt = expiresAtUser;
        Date issued = new Date(issuedAt);
        logger.info("token created for " + userDetails.getUsername());
        if (userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ADMIN"))) {
            logger.info("admin token created for " + userDetails.getUsername());
            expiresAt = expiresAtAdmin;
        }
        Date expires = new Date(expiresAt);
            logger.info("token created at: " + issued);
            logger.info("token will expire at" + expires);
            return Jwts
                    .builder().setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(issued)
                    .setExpiration(expires)
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        }


    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractEmail(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

    }

    public Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
}

