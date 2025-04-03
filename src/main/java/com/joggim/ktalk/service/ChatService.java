package com.joggim.ktalk.service;

import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.ChatRoom;
import com.joggim.ktalk.domain.User;
import com.joggim.ktalk.domain.UserMessage;
import com.joggim.ktalk.dto.AudioRequestDto;
import com.joggim.ktalk.dto.BotMessageDto;
import com.joggim.ktalk.dto.UserMessageDto;
import com.joggim.ktalk.repository.BotMessageRepository;
import com.joggim.ktalk.repository.ChatRoomRepository;
import com.joggim.ktalk.repository.UserMessageRepository;
import com.joggim.ktalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
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
    private UserRepository userRepository;

    // 채팅방 생성 및 메세지 전송
    @Transactional
    public UserMessageDto createChatRoom(AudioRequestDto audio, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        byte[] audioBytes = Base64.getDecoder().decode(audio.getAudio());

        ChatRoom chatRoom = new ChatRoom(user);
        chatRoom = chatRoomRepository.save(chatRoom);

        UserMessage userMessage = null; // AI 서버 호출
        // userMessage = userMessageRepository.save(userMessage);

        return UserMessageDto.of(userMessage, chatRoom.getId());
    }

    // 채팅방 조회
    public List<Object> getMessages(Long chatRoomId) {
        // 엔티티를 조회하면서 createdAt을 포함한 임시 데이터 구조로 변환
        List<MessageWithTimestamp> messages = new ArrayList<>();

        userMessageRepository.findByChatRoomId(chatRoomId)
                .forEach(msg -> messages.add(new MessageWithTimestamp(UserMessageDto.of(msg), msg.getCreatedAt())));

        botMessageRepository.findByChatRoomId(chatRoomId)
                .forEach(msg -> messages.add(new MessageWithTimestamp(BotMessageDto.of(msg), msg.getCreatedAt())));

        // createdAt 기준으로 정렬
        messages.sort(Comparator.comparing(MessageWithTimestamp::createdAt));

        // 정렬된 메시지를 DTO만 반환
        return messages.stream().map(MessageWithTimestamp::message).toList();
    }

    // 메세지 정렬을 위한 임시 데이터 구조
    private record MessageWithTimestamp(Object message, LocalDateTime createdAt) {}

}
