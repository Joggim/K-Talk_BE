package com.joggim.ktalk.service.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joggim.ktalk.dto.RecommendedSentenceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class RecommendationService {

    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public RecommendationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<RecommendedSentenceResponse> fetchRecommendedSentences(String errorType) {
        String url = aiServerUrl + "/recommend-sentences";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> body = Map.of("error_type", errorType);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<List<RecommendedSentenceResponse>>() {}
                );
            } else {
                log.warn("AI 서버 응답 오류: {}", response.getStatusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("추천 문장 요청 실패", e);
            return Collections.emptyList();
        }
    }
}
