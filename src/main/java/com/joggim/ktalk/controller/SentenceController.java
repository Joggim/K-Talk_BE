package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.domain.Sentence;
import com.joggim.ktalk.dto.AudioUrlDto;
import com.joggim.ktalk.dto.FeedbackDto;
import com.joggim.ktalk.dto.SentenceDto;
import com.joggim.ktalk.dto.TextDto;
import com.joggim.ktalk.service.LearningHistoryService;
import com.joggim.ktalk.service.SentenceService;
import com.joggim.ktalk.service.ai.ConvertService;
import com.joggim.ktalk.service.ai.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/sentences")
public class SentenceController {

    @Autowired
    private SentenceService sentenceService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ConvertService convertService;

    @Autowired
    private LearningHistoryService learningHistoryService;

    @GetMapping("/{sentenceId}")
    public ResponseEntity<ApiResponse<SentenceDto>> getSentenceById(@PathVariable Long sentenceId, @AuthenticationPrincipal User principal) {
        String userId = principal.getUsername();
        SentenceDto sentence = sentenceService.getSentenceById(sentenceId, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("문장 조회 성공!", sentence));
    }

    @PostMapping(value = "/{sentenceId}/feedback", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FeedbackDto.Response>> getFeedback(@PathVariable Long sentenceId, @RequestPart("file") MultipartFile audioFile, @AuthenticationPrincipal User principal) {
        String userId = principal.getUsername();
        FeedbackDto feedback = feedbackService.getFeedback(audioFile, sentenceId);
        learningHistoryService.saveLearningResult(userId, feedback);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("발음 피드백 성공!", FeedbackDto.Response.from(feedback)));
    }

    @PostMapping("/{sentenceId}/audio")
    public ResponseEntity<ApiResponse<AudioUrlDto>> generateAudio(@PathVariable Long sentenceId) {
        Sentence sentence = sentenceService.findById(sentenceId);

        if (sentence.getAudioUrl() != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("이미 오디오가 있습니다.", new AudioUrlDto(sentence.getAudioUrl())));
        }

        String audioUrl = convertService.convertTextToSpeech(new TextDto(sentence.getKorean()));
        sentenceService.updateAudioUrl(sentenceId, audioUrl);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("문장 조회 성공!", new AudioUrlDto(audioUrl)));
    }

}
