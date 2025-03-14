package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.dto.SentenceDto;
import com.joggim.ktalk.service.SentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sentences")
public class SentenceController {

    @Autowired
    private SentenceService sentenceService;

    @GetMapping("/{sentenceId}")
    public ResponseEntity<ApiResponse<SentenceDto.SentenceDetail>> getSentenceById(@PathVariable Long sentenceId) {
        SentenceDto.SentenceDetail sentence = sentenceService.getSentenceById(sentenceId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("문장 조회 성공!", sentence));
    }

}
