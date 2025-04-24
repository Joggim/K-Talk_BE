package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, UUID> {
    List<UserMessage> findByChatRoomId(Long chatRoomId);
}