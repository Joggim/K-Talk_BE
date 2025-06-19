package com.joggim.ktalk.service;

import com.joggim.ktalk.domain.BotMessage;
import com.joggim.ktalk.domain.ChatRoom;
import com.joggim.ktalk.domain.User;
import com.joggim.ktalk.domain.UserMessage;
import com.joggim.ktalk.dto.*;
import com.joggim.ktalk.repository.BotMessageRepository;
import com.joggim.ktalk.repository.ChatRoomRepository;
import com.joggim.ktalk.repository.UserMessageRepository;
import com.joggim.ktalk.service.ai.ChatAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserMessageRepository userMessageRepository;
    @Autowired
    private BotMessageRepository botMessageRepository;
    @Autowired
    private ChatAiService chatAiService;

    // 초기 채팅방 생성
    public void createInitialChatRoom(User user) {
        if (!chatRoomRepository.existsByUser(user)) {
            chatRoomRepository.save(new ChatRoom(user));
        }
    }

    // 사용자 채팅방 찾기
    public ChatRoom getChatRoomByUserId(String userId) {
        return chatRoomRepository.findTopByUserUserId(userId);
    }

    // 채팅방 조회
    public List<Object> getMessages(String userId) {
        // 엔티티를 조회하면서 createdAt을 포함한 임시 데이터 구조로 변환
        Long chatRoomId = getChatRoomByUserId(userId).getId();
        List<MessageWithTimestamp> messages = new ArrayList<>();

        userMessageRepository.findByChatRoomId(chatRoomId)
                .forEach(msg -> messages.add(new MessageWithTimestamp(new UserMessageDto.Response(msg), msg.getCreatedAt())));

        botMessageRepository.findByChatRoomId(chatRoomId)
                .forEach(msg -> messages.add(new MessageWithTimestamp(new BotMessageDto.Response(msg), msg.getCreatedAt())));

        // createdAt 기준으로 정렬
        messages.sort(Comparator.comparing(MessageWithTimestamp::createdAt).reversed());

        // 정렬된 메시지를 DTO만 반환
        return messages.stream().map(MessageWithTimestamp::message).toList();
    }

    // 채팅 피드백
    public UserMessageDto.Response processUserMessage(ChatFeedbackRequestDto dto, ChatRoom chatRoom) {
        UserMessageDto.Save saveDto = chatAiService.requestChatFeedback(dto);
        UserMessage saved = userMessageRepository.save(saveDto.toEntity(chatRoom));
        return new UserMessageDto.Response(saved);
    }

    // 채팅 응답
    public BotMessageDto.Response processBotMessage(TextDto textDto, ChatRoom chatRoom) {
        List<ContextDto> context = buildContext(chatRoom);

        ChatReplyRequestDto requestDto = new ChatReplyRequestDto();
        requestDto.setText(textDto.getText());
        requestDto.setContext(context);

        BotMessageDto.Save dto = chatAiService.requestBotResponse(requestDto); // ChatRequestDto 받도록 변경
        BotMessage botMessage = botMessageRepository.save(dto.toEntity(chatRoom));
        return new BotMessageDto.Response(botMessage);
    }

    // 최근 메세지 10개 불러오기
    public List<ContextDto> buildContext(ChatRoom chatRoom) {
        List<ContextMessageWithTimestamp> allMessages = new ArrayList<>();

        userMessageRepository.findByChatRoomId(chatRoom.getId())
                .forEach(m -> allMessages.add(new ContextMessageWithTimestamp("user", m.getContent(), m.getCreatedAt())));

        botMessageRepository.findByChatRoomId(chatRoom.getId())
                .forEach(m -> allMessages.add(new ContextMessageWithTimestamp("bot", m.getContent(), m.getCreatedAt())));

        return allMessages.stream()
                .sorted(Comparator.comparing(ContextMessageWithTimestamp::createdAt))
                .skip(Math.max(0, allMessages.size() - 10))
                .map(m -> new ContextDto(m.role(), m.content()))
                .toList();
    }


    // 메세지 정렬을 위한 임시 데이터 구조
    private record MessageWithTimestamp(Object message, LocalDateTime createdAt) {}
    private record ContextMessageWithTimestamp(String role, String content, LocalDateTime createdAt) {}

}
