package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

    Optional<ErrorLog> findByHistoryId(Long historyId);
}
