package com.joggim.ktalk.security;

import io.jsonwebtoken.*;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Setter
public class JwtTokenProvider {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    // access 토큰 유효 기간 30분
    private long accessTokenValidity = 1800000;
    // refresh 토큰 유효 기간 7일
    private long refreshTokenValidity = 604800000;

    // access 토큰 생성
    public String generateAccessToken(String userId) {
        return generateToken(userId, accessTokenValidity);
    }

    // refresh 토큰 생성
    public String generateRefreshToken(String userId) {
        return generateToken(userId, refreshTokenValidity);
    }

    // JWT 토큰 생성
    private String generateToken(String userId, long validity) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰에서 사용자 ID 추출
    public String getUserIdFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            return parseClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            // 토큰 만료 예외 처리
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰 검증 실패 예외 처리
        }
        return false;
    }

    // JWT 만료 시간 추출
    public LocalDateTime getExpiration(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // JWT parsing
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
