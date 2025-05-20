package com.joggim.ktalk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ErrorAnalysisDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeedbackResponse {
        private String target;
        private String user;
        private String prev;
        private String next;
        private int jamoIndex;
        private int errorIndex;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String target;
        private String user;
        private String prev;
        private String next;
        private int jamoIndex;
    }

    @Data
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String target;
        private String user;
        private String prev;
        private String next;
        private int jamoIndex;
        private String errorType;
        private int errorIndex;
    }
}
