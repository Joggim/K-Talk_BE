package com.joggim.ktalk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackDto {

    private Long sentenceId;

    private boolean passed; // 통과 여부

    private String userText;

    private String userAudioUrl;

    private List<PronunciationError> pronunciationErrors;

}
