package com.joggim.ktalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedSentenceResponse {
    private String content;
    private String translation;
    private String ipa;
}
