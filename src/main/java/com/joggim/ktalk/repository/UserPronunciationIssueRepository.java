package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.PronunciationIssue;
import com.joggim.ktalk.domain.User;
import com.joggim.ktalk.domain.UserPronunciationIssue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPronunciationIssueRepository extends JpaRepository<UserPronunciationIssue, Long> {
    @Query("""
        SELECT upi
        FROM UserPronunciationIssue upi
        JOIN upi.issue pi
        LEFT JOIN ErrorLogPronunciationIssue elpi ON elpi.issue = pi
        LEFT JOIN elpi.errorLog el
        LEFT JOIN el.history h
        LEFT JOIN h.user u
        WHERE upi.user.userId = :userId
          AND u.userId = :userId
        GROUP BY upi
        ORDER BY COUNT(elpi) DESC
    """)
    List<UserPronunciationIssue> findTop10ByUserIdOrderByErrorLogCount(@Param("userId") String userId, Pageable pageable);


    List<UserPronunciationIssue> findByUserUserIdOrderByAccuracyAsc(String userId);

    UserPronunciationIssue findByUserUserIdAndIssueId(String userId, Long issueId);

    boolean existsByUserAndIssue(User user, PronunciationIssue issue);

}
