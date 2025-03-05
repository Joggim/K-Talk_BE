package com.joggim.ktalk.service;

import com.joggim.ktalk.domain.TokenBlacklist;
import com.joggim.ktalk.repository.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenBlacklistService {

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    public void addToBlacklist(String token, LocalDateTime expirationTime) {
        TokenBlacklist blacklistedToken = TokenBlacklist.builder()
                .token(token)
                .expiresAt(expirationTime)
                .build();
        tokenBlacklistRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }
}
