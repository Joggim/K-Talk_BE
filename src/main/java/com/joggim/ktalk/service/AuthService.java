package com.joggim.ktalk.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.User;
import com.joggim.ktalk.dto.TokenDto;
import com.joggim.ktalk.repository.UserRepository;
import com.joggim.ktalk.security.GoogleTokenVerifier;
import com.joggim.ktalk.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatService chatService;

    // 사용자 검증 및 등록
    @Transactional
    public TokenDto authenticateUser(String token) {
        GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(token);

        String userId = payload.getSubject();
        String name = (String) payload.get("name");

        userRepository.findById(userId)
                .orElseGet(() -> {
                    User newUser = userRepository.save(new User(userId, name));
                    chatService.createInitialChatRoom(newUser); // 가입 시 채팅방 생성
                    return newUser;
                });

        String accessToken = jwtTokenProvider.generateAccessToken(userId);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);
        return new TokenDto(accessToken, refreshToken);
    }

    // access 토큰 재발급
    @Transactional
    public TokenDto refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.JWT_TOKEN_INVALID);
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        return new TokenDto(newAccessToken, null);
    }

    // 사용자 로그아웃
    @Transactional
    public void logout(TokenDto token) {
        if (token.getAccessToken() == null && token.getRefreshToken() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        addTokenToBlacklist(token.getAccessToken());
        addTokenToBlacklist(token.getRefreshToken());
    }

    private void addTokenToBlacklist(String token) {
        if (token != null) {
            tokenBlacklistService.addToBlacklist(token, jwtTokenProvider.getExpiration(token));
        }
    }

    public boolean validateToken(String token) {
        if (token == null) {
            return false;
        }

        try {
            return !jwtTokenProvider.isTokenExpired(token) && !tokenBlacklistService.isTokenBlacklisted(token);
        } catch (Exception e) {
            return false;
        }
    }

}
