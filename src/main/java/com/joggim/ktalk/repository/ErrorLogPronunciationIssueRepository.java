package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.ErrorLogPronunciationIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ErrorLogPronunciationIssueRepository extends JpaRepository<ErrorLogPronunciationIssue, Long> {

    @Query("SELECT e FROM ErrorLogPronunciationIssue e WHERE e.issue.id = :issueId AND e.errorLog.history.user.userId = :userId")
    List<ErrorLogPronunciationIssue> findByIssueIdAndUserId(@Param("issueId") Long issueId, @Param("userId") String userId);

}
