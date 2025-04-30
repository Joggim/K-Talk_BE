package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.dto.AudioUrlDto;
import com.joggim.ktalk.dto.TextDto;
import com.joggim.ktalk.service.S3Uploader;
import com.joggim.ktalk.service.ai.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/convert")
public class ConvertController {

    @Autowired
    private ConvertService convertService;

    @Autowired
    private S3Uploader s3Uploader;

    // 텍스트 -> 음성 변환
    @PostMapping(value = "/tts")
    public ResponseEntity<ApiResponse<AudioUrlDto>> convertTTS(@RequestBody TextDto dto) {
        String text = dto.getText();

        if (text == null || text.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        String audioUrl = convertService.convertTextToSpeech(dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("TTS 변환 성공!", new AudioUrlDto(audioUrl)));
    }

    // 음성 -> 텍스트 변환
    @PostMapping(value = "/stt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TextDto>> convertSTT(@RequestParam("file")MultipartFile audioFile){
        if (audioFile == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        TextDto textDto = convertService.convertSpeechToText(audioFile);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("음성 텍스트 변환 성공!", textDto));
    }

}
