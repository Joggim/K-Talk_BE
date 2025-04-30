package com.joggim.ktalk.service.ai;

import com.joggim.ktalk.common.util.AudioFileUtil;
import com.joggim.ktalk.dto.TextDto;
import com.joggim.ktalk.service.S3Uploader;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@Service
public class ConvertService {

    @Autowired
    private S3Uploader s3Uploader;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    // 텍스트 -> 음성 변환
    public String convertTextToSpeech(TextDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TextDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                aiServerUrl + "/tts",
                HttpMethod.POST,
                request,
                byte[].class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("TTS 요청 실패");
        }

        InputStream inputStream = new ByteArrayInputStream(response.getBody());

        return s3Uploader.uploadMp3(inputStream, "tts");
    }

    // 음성 -> 텍스트 변환
    public TextDto convertSpeechToText(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 파일을 ByteArrayResource로 감싸기
        ByteArrayResource resource = AudioFileUtil.toByteArrayResource(file);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            String sttUrl = aiServerUrl + "/stt/whisper";
            ResponseEntity<Map> response = restTemplate.postForEntity(sttUrl, requestEntity, Map.class);

            // 응답에서 transcription 필드 추출
            Map<String, Object> responseBody = response.getBody();
            String transcription = (String) responseBody.get("transcription");

            return new TextDto(transcription);
        } catch (RestClientException e) {
            throw new RuntimeException("AI 서버 호출 실패", e);
        }
    }
}