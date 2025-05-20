package com.joggim.ktalk.service;

import com.joggim.ktalk.dto.ErrorAnalysisDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ErrorClassificationService {

    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    @Autowired
    private RestTemplate restTemplate;

    public List<ErrorAnalysisDto.Response> classifyError(List<ErrorAnalysisDto.FeedbackResponse> errorAnalysis) {
        // 1. classify 요청용 request DTO 추출
        List<ErrorAnalysisDto.Request> requestList = errorAnalysis.stream()
                .map(e -> new ErrorAnalysisDto.Request(
                        e.getTarget(), e.getUser(), e.getPrev(), e.getNext(), e.getJamoIndex()
                )).toList();

        // 2. API 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<ErrorAnalysisDto.Request>> requestEntity = new HttpEntity<>(requestList, headers);

        ResponseEntity<ErrorAnalysisDto.Response[]> response = restTemplate.postForEntity(
                aiServerUrl + "/classify",
                requestEntity,
                ErrorAnalysisDto.Response[].class
        );

        // 3. errorIndex를 응답 결과에 붙이기
        ErrorAnalysisDto.Response[] responseArray = response.getBody();
        List<ErrorAnalysisDto.Response> mergedResult = new ArrayList<>();

        for (int i = 0; i < responseArray.length; i++) {
            ErrorAnalysisDto.Response res = responseArray[i];
            int errorIndex = errorAnalysis.get(i).getErrorIndex();
            res.setErrorIndex(errorIndex);
            mergedResult.add(res);
        }

        return mergedResult;
    }

}