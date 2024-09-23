package com.sparta.logistics.client.user.repository;

import com.sparta.logistics.client.user.model.Message;
import com.sparta.logistics.client.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    Page<Message> findBySenderAndReceiverAndMessageContaining(User sender, User receiver, String context, Pageable pageable);
}
