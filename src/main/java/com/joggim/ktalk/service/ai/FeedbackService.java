package com.joggim.ktalk.service.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.domain.Sentence;
import com.joggim.ktalk.dto.ErrorAnalysisDto;
import com.joggim.ktalk.dto.FeedbackDto;
import com.joggim.ktalk.dto.PronunciationError;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackService {

    @Autowired
    private SentenceRepository sentenceRepository;

    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    public FeedbackDto getFeedback(MultipartFile audioFile, Long sentenceId) {

        Sentence sentence = sentenceRepository.findById(sentenceId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        String reference = sentence.getKorean();

        ByteArrayResource resource;
        try {
            resource = new ByteArrayResource(audioFile.getBytes()) {
                @Override
                public String getFilename() {
                    return audioFile.getOriginalFilename();  // 또는 "audio.wav"
                }
            };
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CONVERT_FAIL);  // 예외 정의해두면 좋아요
        }

        MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
        request.add("file", resource);
        request.add("reference", reference);

        // Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String FeedbackUrl = aiServerUrl + "/feedback/pronunciation";
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    FeedbackUrl,
                    requestEntity,
                    Map.class
            );

            ObjectMapper mapper = new ObjectMapper();

            // errorAnalysis 추출 및 변환
            List<ErrorAnalysisDto.FeedbackResponse> errorAnalysis = mapper.convertValue(
                    response.getBody().get("errorAnalysis"),
                    new TypeReference<List<ErrorAnalysisDto.FeedbackResponse>>() {}
            );

            // pronunciationErrors도 형변환 필요
            List<PronunciationError> pronunciationErrors = mapper.convertValue(
                    response.getBody().get("pronunciationErrors"),
                    new TypeReference<List<PronunciationError>>() {}
            );

            return FeedbackDto.builder()
                    .sentenceId(sentenceId)
                    .userText((String) response.getBody().get("userText"))
                    .userIpa((String) response.getBody().get("userIpa"))
                    .pronunciationErrors(pronunciationErrors)
                    .passed((boolean) response.getBody().get("passed"))
                    .errorAnalysis(errorAnalysis)
                    .build();
        } catch (RestClientException e) {
            throw new CustomException(ErrorCode.AI_SERVER_ERROR);
        }
    }
}
