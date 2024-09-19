package com.sparta.logistics.client.user.model;

import com.sparta.logistics.client.user.common.BaseEntity;
import com.sparta.logistics.client.user.enums.DeliveryManagerType;
import com.sparta.logistics.common.model.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity(name = "p_messages")
@Getter
@NoArgsConstructor
@ToString
public class Message extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id")
    private UUID id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(name = "message")
    private String message;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    public void softDelete() {
        this.isDeleted = true;
    }

    public Message(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }
    public static Message createMessage(User sender, User receiver, String message) {
        return new Message(sender, receiver, message);
    }
}
