package com.ranjit.todo.todo.entities;

import com.ranjit.todo.todo.enums.MessageStatusEnum;
import com.ranjit.todo.todo.enums.MessageTypesEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String messageClientId;

    @Column(nullable = false)
    private String content;

//    @Column(name="sender_id" , nullable = false)
//    private Long senderId;
//
//    @Column(name="receiver_id",nullable = false)
//    private Long receiverId;

    @Column(nullable = false)
    private Long timestamp = System.currentTimeMillis();

    private Boolean isDeleted = false;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private MessageTypesEnum messageType = MessageTypesEnum.TEXT;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private MessageStatusEnum status = MessageStatusEnum.SENT;

    // Joins
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)

    private UserEntity receiver;
}
