package com.joggim.ktalk.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorLogPronunciationIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 몇 번째 글자에서 발생한 오류인지
    private int errorIndex;

    // 연관된 ErrorLog
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "error_log_id")
    private ErrorLog errorLog;

    // 연관된 PronunciationIssue
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pronunciation_issue_id")
    private PronunciationIssue issue;

}
