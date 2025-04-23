package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 대화 목록 조회

    // 대화 시작

    // 채팅방 조회
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ApiResponse<List<Object>>> getMessages(@PathVariable Long chatRoomId) {
        List<Object> messages = chatService.getMessages(chatRoomId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("채팅방 조회 성공!", messages));
    }

}
