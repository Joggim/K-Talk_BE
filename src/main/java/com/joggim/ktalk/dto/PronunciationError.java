package com.joggim.ktalk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PronunciationError {
    private String wrong; // 틀린 글자
    private String correct; // 정답 글자
    private int index; // 틀린 글자 위치 위치
}