package com.joggim.ktalk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackDto {

    private Long sentenceId;

    private boolean passed; // 통과 여부

    private String userText;

    private String userAudioUrl;

    private List<PronunciationError> pronunciationErrors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PronunciationError {
        private String wrong; // 틀린 글자
        private String correct; // 정답 글자
        private int index; // 틀린 글자 위치 위치
    }

}
