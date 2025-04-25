package com.joggim.ktalk.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.common.util.AudioFileUtil;
import com.joggim.ktalk.dto.BotMessageDto;
import com.joggim.ktalk.dto.ChatFeedbackRequestDto;
import com.joggim.ktalk.dto.TextDto;
import com.joggim.ktalk.dto.UserMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatAiService {

    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 사용자 음성과 텍스트를 AI 서버에 보내 피드백 받기
    public UserMessageDto.Save requestChatFeedback(ChatFeedbackRequestDto dto) {
        MultipartFile audioFile = dto.getAudioFile();
        String transcription = dto.getTranscription();

        ByteArrayResource audioResource = AudioFileUtil.toByteArrayResource(audioFile);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", audioResource);
        requestBody.add("transcription", transcription);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            String url = aiServerUrl + "/chat/feedback";
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            Map<String, Object> body = response.getBody();

            UserMessageDto.ChatFeedbackDto feedback = objectMapper.convertValue(body.get("feedback"), UserMessageDto.ChatFeedbackDto.class);

            UserMessageDto.Save saveDto = new UserMessageDto.Save();
            saveDto.setContent((String) body.get("content"));
            saveDto.setUserAudioUrl((String) body.get("userAudioUrl"));
            saveDto.setModelAudioUrl((String) body.get("modelAudioUrl"));
            saveDto.setFeedback(feedback);

            return saveDto;
        } catch (RestClientException e) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR);
        }
    }

    public BotMessageDto.Save requestBotResponse(TextDto textDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TextDto> requestEntity = new HttpEntity<>(textDto, headers);

        try {
            String url = aiServerUrl + "/chat/reply"; // 예시
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            Map<String, Object> body = response.getBody();

            BotMessageDto.Save dto = new BotMessageDto.Save();
            dto.setContent((String) body.get("content"));
            dto.setTranslation((String) body.get("translation"));
            dto.setModelAudioUrl((String) body.get("modelAudioUrl"));
            return dto;
        } catch (RestClientException e) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR);
        }
    }
}