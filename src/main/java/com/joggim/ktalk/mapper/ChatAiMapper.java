package com.joggim.ktalk.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joggim.ktalk.dto.ChatFeedbackRequestDto;
import com.joggim.ktalk.dto.UserMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatAiMapper {

    private final ObjectMapper objectMapper;

    public UserMessageDto.Save toUserMessageSaveDto(Map<String, Object> aiResponse) {
        // 피드백 변환
        UserMessageDto.ChatFeedbackDto feedback =
                objectMapper.convertValue(aiResponse.get("feedback"), UserMessageDto.ChatFeedbackDto.class);

        UserMessageDto.Save saveDto = new UserMessageDto.Save();
        saveDto.setContent((String) aiResponse.get("content"));
        saveDto.setUserAudioUrl((String) aiResponse.get("userAudioUrl"));
        saveDto.setModelAudioUrl((String) aiResponse.get("modelAudioUrl"));
        saveDto.setFeedback(feedback);
        return saveDto;
    }
}
