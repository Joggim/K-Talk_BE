package com.joggim.ktalk.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ChatFeedbackRequestDto {

    private String transcription; // STT 또는 입력된 텍스트

    private MultipartFile audioFile;

}