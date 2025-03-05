package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.dto.UserDto;
import com.joggim.ktalk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 사용자 조회
    @GetMapping
    public ResponseEntity<ApiResponse<UserDto>> getUser(@AuthenticationPrincipal User principal) {
        String userId = principal.getUsername();
        UserDto user = userService.getUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("사용자 조회 성공!", user));
    }

    // 사용자 수정
    @PatchMapping
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@AuthenticationPrincipal User principal, @RequestBody UserDto dto) {
        String userId = principal.getUsername();
        UserDto user = userService.updateNickname(userId, dto.getNickname());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("사용자 수정 성공!", user));
    }

    // 사용자 탈퇴
    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deleteUser(@AuthenticationPrincipal User principal) {
        String userId = principal.getUsername();
        userService.deleteUser(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("사용자 탈퇴 완료!", null));
    }
}
