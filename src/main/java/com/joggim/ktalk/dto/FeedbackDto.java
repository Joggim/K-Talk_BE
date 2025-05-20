package com.joggim.ktalk.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {

    private Long sentenceId;
    private boolean passed;
    private String userText;
    private String userIpa;
    private List<PronunciationError> pronunciationErrors;
    private List<ErrorAnalysisDto.FeedbackResponse> errorAnalysis;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long sentenceId;
        private boolean passed; // 통과 여부
        private String userText;
        private String userIpa;
        private List<PronunciationError> pronunciationErrors;

        public static Response from(FeedbackDto dto) {
            return Response.builder()
                    .sentenceId(dto.getSentenceId())
                    .userText(dto.getUserText())
                    .userIpa(dto.getUserIpa())
                    .passed(dto.isPassed())
                    .pronunciationErrors(dto.getPronunciationErrors())
                    .build();
        }
    }

}
