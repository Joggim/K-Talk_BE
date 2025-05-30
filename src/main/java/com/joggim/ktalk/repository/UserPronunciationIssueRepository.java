package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.PronunciationIssue;
import com.joggim.ktalk.domain.User;
import com.joggim.ktalk.domain.UserPronunciationIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPronunciationIssueRepository extends JpaRepository<UserPronunciationIssue, Long> {
    List<UserPronunciationIssue> findByUserUserIdOrderByAccuracyAsc(String userId);

    UserPronunciationIssue findByUserUserIdAndIssueId(String userId, Long issueId);

    boolean existsByUserAndIssue(User user, PronunciationIssue issue);

}
