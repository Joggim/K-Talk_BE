package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.dto.AudioRequestDto;
import com.joggim.ktalk.dto.UserMessageDto;
import com.joggim.ktalk.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 대화 목록 조회

    // 대화 시작
    @PostMapping
    public ResponseEntity<ApiResponse<UserMessageDto>> createChatRoom(AudioRequestDto dto, @AuthenticationPrincipal User user) {
        String userId = user.getUsername();
        UserMessageDto message = chatService.createChatRoom(dto, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("새로운 채팅 생성 성공!", message));
    }

    // 채팅방 조회
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ApiResponse<List<Object>>> getMessages(@PathVariable Long chatRoomId) {
        List<Object> messages = chatService.getMessages(chatRoomId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("채팅방 조회 성공!", messages));
    }

}
