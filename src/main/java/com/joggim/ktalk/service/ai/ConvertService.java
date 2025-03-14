package com.joggim.ktalk.service.ai;

import com.joggim.ktalk.dto.TextDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class ConvertService {

    // 텍스트 -> 음성 변환
    public InputStream convertTextToSpeech(TextDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TextDto> requestEntity = new HttpEntity<>(dto, headers);

        // AI 서버 호출

        return InputStream.nullInputStream();
    }

    // 음성 -> 텍스트 변환
    public String convertSpeechToText(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultipartFile> requestEntity = new HttpEntity<>(file, headers);

        // AI 서버 호출

        return "변환된 텍스트";
    }
}