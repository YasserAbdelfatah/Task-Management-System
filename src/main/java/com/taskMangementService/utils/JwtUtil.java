package com.taskMangementService.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Component
public class JwtUtil {
    private static final int BEARER_INDEX = Constants.HEADER_AUTHORIZATION_PREFIX.length();

    @Value("${jwt.secret.key}")
    private String jwtSecret;
    @Value("${jwt.expiration.hours}")
    private long expirationInHours;

    public String createJWTToken(String username, String role) {
        ZonedDateTime now = ZonedDateTime.now();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(now.plusHours(expirationInHours).toInstant()))
                .signWith(getSignInKey(jwtSecret))
                .claim(Constants.JWT_CLAIM_USER_ROLES, role)
                .compact();
    }

    private static SecretKey getSignInKey(String secretKey) {
//        SecretKey key = Jwts.SIG.HS512.key().build();
//        String secretString = Encoders.BASE64.encode(key.getEncoded());

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public Optional<String> getTokenWithoutBearer(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(Constants.HEADER_AUTHORIZATION))
                .map(this::getTokenWithoutBearer);
    }

    private String getTokenWithoutBearer(String tokenWithBearerPrefix) {
        return StringUtils.substring(tokenWithBearerPrefix, BEARER_INDEX);
    }

    public boolean verifyToken(String token) {
        try {
            ZonedDateTime now = ZonedDateTime.now();
            Claims claims = extractAllClaims(token);
            ZonedDateTime expiration = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault());
            return now.isBefore(expiration);
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace -> {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            log.error("Signature validation failed");
        } catch (Exception e) {
            log.error("Exception while parsing JWT token: {}", e.getMessage());
        }

        return false;
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey(jwtSecret)).build()
                .parseSignedClaims(token)
                .getPayload();
    }
}