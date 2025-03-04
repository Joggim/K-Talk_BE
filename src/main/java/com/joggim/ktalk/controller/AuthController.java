package com.joggim.ktalk.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.dto.GoogleLoginRequestDto;
import com.joggim.ktalk.dto.TokenDto;
import com.joggim.ktalk.dto.UserDto;
import com.joggim.ktalk.security.GoogleTokenVerifier;
import com.joggim.ktalk.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 구글 로그인
    @PostMapping("/google-login")
    public ResponseEntity<ApiResponse<TokenDto>> googleLogin(@RequestBody GoogleLoginRequestDto loginRequest) {
        TokenDto tokenDto = authService.authenticateUser(loginRequest.getToken());
        return ResponseEntity.ok(ApiResponse.success("로그인 성공!", tokenDto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestBody TokenDto tokenDto) {
        authService.logout(tokenDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("로그아웃 성공!", null));
    }

    // 토큰 갱신
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenDto>> refreshAccessToken(@RequestBody TokenDto refreshTokenRequest) {
        TokenDto newAccessToken = authService.refreshAccessToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("토큰 갱신 성공!", newAccessToken));
    }

}
