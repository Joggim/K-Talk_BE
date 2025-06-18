package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.dto.LearningHistoryDto;
import com.joggim.ktalk.service.LearningHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/learning-history")
@RequiredArgsConstructor
public class LearningHistoryController {

    @Autowired
    private LearningHistoryService learningHistoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LearningHistoryDto>>> getLearningHistory(@AuthenticationPrincipal User principal) {
        String userId = principal.getUsername();
        List<LearningHistoryDto> histories = learningHistoryService.getHistoryByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("학습 이력 조회 성공", histories));
    }
}
