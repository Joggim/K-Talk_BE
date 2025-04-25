package com.joggim.ktalk.service;

import com.joggim.ktalk.domain.BotMessage;
import com.joggim.ktalk.domain.ChatRoom;
import com.joggim.ktalk.domain.User;
import com.joggim.ktalk.domain.UserMessage;
import com.joggim.ktalk.dto.BotMessageDto;
import com.joggim.ktalk.dto.ChatFeedbackRequestDto;
import com.joggim.ktalk.dto.TextDto;
import com.joggim.ktalk.dto.UserMessageDto;
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
        BotMessageDto.Save dto = chatAiService.requestBotResponse(textDto);
        BotMessage botMessage = botMessageRepository.save(dto.toEntity(chatRoom));
        return new BotMessageDto.Response(botMessage);
    }


    // 메세지 정렬을 위한 임시 데이터 구조
    private record MessageWithTimestamp(Object message, LocalDateTime createdAt) {}


//    // 채팅방 생성 및 메세지 전송
//    @Transactional
//    public UserMessageDto createChatRoom(AudioRequestDto audio, String userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//
//        byte[] audioBytes = Base64.getDecoder().decode(audio.getAudio());
//
//        ChatRoom chatRoom = new ChatRoom(user);
//        chatRoom = chatRoomRepository.save(chatRoom);
//
//        UserMessage userMessage = null; // AI 서버 호출
//        // userMessage = userMessageRepository.save(userMessage);
//
//        return UserMessageDto.of(userMessage, chatRoom.getId());
//    }
}
