package com.joggim.ktalk.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatReplyRequestDto {
    private String text;              // 사용자가 입력한 문장
    private List<ContextDto> context; // 과거 대화 기록 (role + content)
}
