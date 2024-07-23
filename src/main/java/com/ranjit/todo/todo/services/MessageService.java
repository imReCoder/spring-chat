package com.ranjit.todo.todo.services;

import com.ranjit.todo.todo.dtos.ChatMessageDTO;
import com.ranjit.todo.todo.dtos.MessageUpdateDTO;
import com.ranjit.todo.todo.dtos.UserDTO;
import com.ranjit.todo.todo.entities.MessageEntity;
import com.ranjit.todo.todo.entities.UserEntity;
import com.ranjit.todo.todo.enums.MessageStatusEnum;
import com.ranjit.todo.todo.repositories.MessageRepository;
import com.ranjit.todo.todo.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final Logger _logger = org.slf4j.LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository _messageRepository;
    private final UserService _userService;

    public MessageService(MessageRepository messageRepository, UserService userService) {
        _messageRepository = messageRepository;
        _userService = userService;
    }

    public MessageEntity saveMessage(ChatMessageDTO incomingMessage) {
        MessageEntity messageEntity = new MessageEntity();
        UserEntity sender = _userService.getUserEntityById(Long.parseLong(incomingMessage.getSenderId()));
        UserEntity receiver = _userService.getUserEntityById(Long.parseLong(incomingMessage.getReceiverId()));

        messageEntity.setSender(sender);
        messageEntity.setReceiver(receiver);

        messageEntity.setContent(incomingMessage.getContent());
        messageEntity.setMessageClientId(incomingMessage.getMessageClientId());
        _logger.info("Saving message: " + messageEntity.toString());
        return _messageRepository.save(messageEntity);
    }

    ;

    public List<MessageEntity> getAllMessages() {
        List<MessageEntity> messages = _messageRepository.findAll();
        _logger.info("Messages: " + messages);
        return messages;
    }

    public MessageEntity getMessageById(Long id) {
        Optional<MessageEntity> messageOptional = _messageRepository.findById(id);
        if (messageOptional.isEmpty()) {
            _logger.error("Message not found");
            return null;
        }
        return messageOptional.get();
    }

    @Transactional
    public MessageEntity updateMessage(MessageUpdateDTO messageUpdate) {
        Optional<MessageEntity> messageOptional = _messageRepository.findById(Long.parseLong(messageUpdate.getMessageId()));
        if (messageOptional.isEmpty()) {
            _logger.error("Message not found");
            return null;
        }
        MessageEntity message = messageOptional.get();

// Initialize the sender proxy
        Hibernate.initialize(message.getSender());
        Hibernate.initialize(message.getReceiver());


// Now you can safely access sender.name
        message.setStatus(messageUpdate.getStatus());
        return _messageRepository.save(message);
    }

    public void updateAllPreviousMessages(MessageEntity message) {
//     fetch all the messages sent by the sender to the receiver with timestamp less than the current message
        Long senderId = message.getSender().getId();
        Long receiverId = message.getReceiver().getId();
        List<MessageEntity> previousMessages = _messageRepository.findBySenderIdAndReceiverIdAndTimestampLessThan(senderId, receiverId, message.getTimestamp());
        this._logger.info("Found previous messages: {}", previousMessages.size());
        for (MessageEntity previousMessage : previousMessages) {
            previousMessage.setStatus(MessageStatusEnum.DELIVERED);
            _messageRepository.save(previousMessage);
        }
    }

    public void updateMessages(List<MessageUpdateDTO> messageUpdates) {
        for (MessageUpdateDTO messageUpdate : messageUpdates) {
            updateMessage(messageUpdate);
        }
    }

    public List<MessageEntity> getUndeliveredMessages(String userId) {
        return _messageRepository.findByReceiverIdAndStatusAndIsDeletedFalse(Long.parseLong(userId), MessageStatusEnum.SENT);
    }

    public List<MessageEntity> getMessagesLatestStatus(String userId, List<String> messageIds) {
        List<Long> messageIdsLong = messageIds.stream().map(Long::parseLong).toList();
        return _messageRepository.findMessagesByIdsAndSenderId(messageIdsLong, Long.parseLong(userId));
    }

    public void deleteMessages(List<Long> messageIds) {
        _logger.info("Deleting messages: {}", messageIds);
        for (Long messageId : messageIds) {
            _messageRepository.deleteById(messageId);
        }
    }

    @Transactional
    public void deleteMessagesByStatus() {
        _logger.info("Deleting read messages");
        _messageRepository.deleteAllByStatus(MessageStatusEnum.READ);
    }
}
