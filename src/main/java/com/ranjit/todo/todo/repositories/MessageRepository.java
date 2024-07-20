package com.ranjit.todo.todo.repositories;

import com.ranjit.todo.todo.entities.MessageEntity;
import com.ranjit.todo.todo.enums.MessageStatusEnum;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    @EntityGraph(value = "Message.withUsers", type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"sender", "receiver"})
    List<MessageEntity> findAll();

    @EntityGraph(value = "Message.withUsers", type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"sender", "receiver"})
    Optional<MessageEntity> findById(Long id);

    //    @Query("SELECT m.id AS id, m.sender AS senderId, m.receiver AS receiverId , m.timestamp as timestamp,m.content as content FROM MessageEntity m")
//    List<ChatMessageDTO> getAllMessages();
//    ger undelivered  messages for a user
//    @Query("SELECT m FROM MessageEntity m WHERE m.receiver_id = :userId AND m.status = 1")
    @EntityGraph(value = "Message.withUsers", type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"sender", "receiver"})
    List<MessageEntity> findByReceiverIdAndStatusAndIsDeletedFalse(Long receiverId, MessageStatusEnum status);

    @EntityGraph(value = "Message.withUsers", type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"sender", "receiver"})
    List<MessageEntity> findBySenderIdAndReceiverIdAndTimestampLessThan(Long senderId, Long receiverId, Long timestamp);

    @EntityGraph(value = "Message.withUsers", type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"sender", "receiver"})
    @Query("SELECT m FROM MessageEntity m WHERE m.id IN :ids AND m.sender.id = :senderId")
    List<MessageEntity> findMessagesByIdsAndSenderId(@Param("ids") List<Long> ids, @Param("senderId") Long senderId);
}
