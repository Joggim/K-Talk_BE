package com.joggim.ktalk.service.ai;

import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.dto.ChatFeedbackRequestDto;
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

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatAiService {

    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    @Autowired
    private RestTemplate restTemplate;

    // 사용자 음성과 텍스트를 AI 서버에 보내 피드백 받기
    public Map<String, Object> requestChatFeedback(ChatFeedbackRequestDto dto) {
        MultipartFile audioFile = dto.getAudioFile();
        String transcription = dto.getTranscription();

        ByteArrayResource audioResource;
        try {
            audioResource = new ByteArrayResource(audioFile.getBytes()) {
                @Override
                public String getFilename() {
                    return audioFile.getOriginalFilename();
                }
            };
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CONVERT_FAIL);
        }

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", audioResource);
        requestBody.add("transcription", transcription);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            String url = aiServerUrl + "/chat/feedback";
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR);
        }
    }
}