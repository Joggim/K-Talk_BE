package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import com.joggim.ktalk.dto.TextDto;
import com.joggim.ktalk.service.ai.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/convert")
public class ConvertController {

    @Autowired
    private ConvertService convertService;

    // 텍스트 -> 음성 변환
    @PostMapping(value = "/tts", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> convertTTS(@RequestBody TextDto dto) {
        String text = dto.getText();

        if (text == null || text.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        InputStream audioStream = convertService.convertTextToSpeech(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/wav"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(new InputStreamResource(audioStream));
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
