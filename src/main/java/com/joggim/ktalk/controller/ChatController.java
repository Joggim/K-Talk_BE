package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.domain.ChatRoom;
import com.joggim.ktalk.dto.BotMessageDto;
import com.joggim.ktalk.dto.ChatFeedbackRequestDto;
import com.joggim.ktalk.dto.TextDto;
import com.joggim.ktalk.dto.UserMessageDto;
import com.joggim.ktalk.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    // 사용자 메세지 피드백 요청
    @PostMapping(value = "/feedback", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserMessageDto.Response>> processUserMessage(
            @ModelAttribute ChatFeedbackRequestDto dto,
            @AuthenticationPrincipal User principal
    ) {
        String userId = principal.getUsername();
        ChatRoom chatRoom = chatService.getChatRoomByUserId(userId);

        UserMessageDto.Response response = chatService.processUserMessage(dto, chatRoom);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("챗봇 피드백 성공!", response));
    }

    // 챗봇 응답 요청
    @PostMapping("/reply")
    public ResponseEntity<ApiResponse<BotMessageDto.Response>> getBotMessage(
            @RequestBody TextDto textDto,
            @AuthenticationPrincipal User principal
    ) {
        String userId = principal.getUsername();
        ChatRoom chatRoom = chatService.getChatRoomByUserId(userId);

        BotMessageDto.Response response = chatService.processBotMessage(textDto, chatRoom);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("챗봇 응답 성공!", response));
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
