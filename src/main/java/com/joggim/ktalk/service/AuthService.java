package com.joggim.ktalk.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.User;
import com.joggim.ktalk.dto.UserDto;
import com.joggim.ktalk.repository.UserRepository;
import com.joggim.ktalk.security.GoogleTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    @Autowired
    private UserRepository userRepository;

    // 사용자 검증 및 등록
    @Transactional
    public UserDto authenticateUser(String token) {

        GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(token);

        String userId = payload.getSubject();
        String name = (String) payload.get("name");

        User user = userRepository.findById(userId)
                .orElseGet(() -> userRepository.save(new User(userId, name)));
        return UserDto.fromEntity(user);
    }

}
