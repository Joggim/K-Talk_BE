package com.joggim.ktalk.repository;

import com.joggim.ktalk.domain.ChatRoom;
import com.joggim.ktalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    boolean existsByUser(User user);
    ChatRoom findTopByUserUserId(String userId);
}