package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {

    boolean existsByToken(String token);

}
