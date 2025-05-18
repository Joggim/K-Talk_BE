package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.dto.PronunciationIssueDto;
import com.joggim.ktalk.service.PronunciationIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pronunciation-issue")
public class PronunciationIssueController {

    @Autowired
    private PronunciationIssueService pronunciationIssueService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PronunciationIssueDto.Summary>>> getRecommendations(@AuthenticationPrincipal User principal) {
        String userId = principal.getUsername();
        List<PronunciationIssueDto.Summary> result = pronunciationIssueService.getRecommendations(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("학습 추천 경로 조회 성공!",result));
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<ApiResponse<PronunciationIssueDto.Detail>> getRecommendationDetail(@PathVariable Long issueId, @AuthenticationPrincipal User principal) {
        String userId = principal.getUsername();
        PronunciationIssueDto.Detail result = pronunciationIssueService.getRecommendationDetail(userId, issueId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("학습 추천 상세 조회 성공!",result));
    }
}
