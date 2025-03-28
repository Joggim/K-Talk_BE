package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.dto.FeedbackDto;
import com.joggim.ktalk.service.ai.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/learning")
public class LearningController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseEntity<ApiResponse<FeedbackDto>> evaluatePronunciation(
            @RequestPart("audio") MultipartFile audioFile,
            @RequestPart(value = "sentence", required = false) String sentence) {
        FeedbackDto response = feedbackService.getFeedback(audioFile, sentence);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("발음 피드백 성공!", response));
    }

}
