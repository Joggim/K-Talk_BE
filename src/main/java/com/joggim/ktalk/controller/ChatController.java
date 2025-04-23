package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
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

    // 채팅방 조회
    @GetMapping("/messages")
    public ResponseEntity<ApiResponse<List<Object>>> getMessages(@AuthenticationPrincipal User principal) {
        String userId = principal.getUsername();
        List<Object> messages = chatService.getMessages(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("채팅방 조회 성공!", messages));
    }


//    // 대화 시작
//    @PostMapping
//    public ResponseEntity<ApiResponse<UserMessageDto>> createChatRoom(AudioRequestDto dto, @AuthenticationPrincipal User user) {
//        String userId = user.getUsername();
//        UserMessageDto message = chatService.createChatRoom(dto, userId);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(ApiResponse.success("새로운 채팅 생성 성공!", message));
//    }

}
