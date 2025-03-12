package com.joggim.ktalk.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {

    private String originalText;

    private String recognizedText;

    private double accuracy;

    private FeedbackDetail feedback;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedbackDetail {
        private List<PronunciationError> errors;
        private String explanation; // 텍스트 형태의 피드백 메시지
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PronunciationError {
        private String character; // 오류가 있는 글자
        private int index; // 위치
    }

}
