package com.joggim.ktalk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContextDto {
    private String role;    // "user" 또는 "bot"
    private String content; // 메시지 본문
}
