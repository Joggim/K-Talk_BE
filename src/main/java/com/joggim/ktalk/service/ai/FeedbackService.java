package com.joggim.ktalk.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.Sentence;
import com.joggim.ktalk.dto.AudioRequestDto;
import com.joggim.ktalk.dto.FeedbackDto;
import com.joggim.ktalk.repository.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {

    @Autowired
    private SentenceRepository sentenceRepository;

    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    public FeedbackDto getFeedback(AudioRequestDto audio, Long sentenceId) {

        Sentence sentence = sentenceRepository.findById(sentenceId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        String reference = sentence.getKorean();

        byte[] audioBytes = Base64.getDecoder().decode(audio.getAudio());
        ByteArrayResource resource = new ByteArrayResource(audioBytes) {
            @Override
            public String getFilename() {
                return "audio.wav";
            }
        };

        MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
        request.add("file", resource);
        request.add("reference", reference);

        // Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String FeedbackUrl = aiServerUrl + "/evaluate";
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    FeedbackUrl,
                    requestEntity,
                    Map.class
            );

            FeedbackDto feedbackDto = FeedbackDto
                    .builder()
                    .userText((String) response.getBody().get("userText"))
                    .pronunciationErrors((List<FeedbackDto.PronunciationError>) response.getBody().get("pronunciationErrors"))
                    .passed((boolean) response.getBody().get("passed"))
                    .build();
            return feedbackDto;
        } catch (RestClientException e) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR);
        }
    }
}
