package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.dto.AudioRequestDto;
import com.joggim.ktalk.dto.FeedbackDto;
import com.joggim.ktalk.dto.SentenceDto;
import com.joggim.ktalk.service.SentenceService;
import com.joggim.ktalk.service.ai.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sentences")
public class SentenceController {

    @Autowired
    private SentenceService sentenceService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/{sentenceId}")
    public ResponseEntity<ApiResponse<SentenceDto>> getSentenceById(@PathVariable Long sentenceId) {
        SentenceDto sentence = sentenceService.getSentenceById(sentenceId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("문장 조회 성공!", sentence));
    }

    @PostMapping("/{sentenceId}/feedback")
    public ResponseEntity<ApiResponse<FeedbackDto>> getFeedback(@PathVariable Long sentenceId, @RequestBody AudioRequestDto audio) {
        FeedbackDto feedback = feedbackService.getFeedback(audio, sentenceId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("발음 피드백 성공!", feedback));
    }

}
