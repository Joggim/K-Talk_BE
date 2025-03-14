package com.joggim.ktalk.controller;

import com.joggim.ktalk.common.ApiResponse;
import com.joggim.ktalk.dto.SentenceDto;
import com.joggim.ktalk.dto.TopicDto;
import com.joggim.ktalk.service.SentenceService;
import com.joggim.ktalk.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private SentenceService sentenceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TopicDto>>> getTopics() {
        List<TopicDto> topics = topicService.getTopics();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("주제 목록 조회 성공!", topics));
    }

    @GetMapping("/{topicId}/sentences")
    public ResponseEntity<ApiResponse<List<SentenceDto.SentenceSummary>>> getSentenceByTopic(@PathVariable Long topicId) {
        List<SentenceDto.SentenceSummary> sentences = sentenceService.getSentencesByTopic(topicId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("문장 목록 조회 성공!", sentences));
    }

}
