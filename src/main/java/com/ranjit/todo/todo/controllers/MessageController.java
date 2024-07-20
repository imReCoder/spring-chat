package com.ranjit.todo.todo.controllers;

import com.ranjit.todo.todo.dtos.ChatMessageDTO;
import com.ranjit.todo.todo.dtos.MessageIdsDTO;
import com.ranjit.todo.todo.dtos.MessageUpdateDTO;
import com.ranjit.todo.todo.dtos.ResponseBody;
import com.ranjit.todo.todo.entities.MessageEntity;
import com.ranjit.todo.todo.enums.MessageStatusEnum;
import com.ranjit.todo.todo.services.MessageService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/messages")
public class MessageController {
    Logger _logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageService _messageService;
    private final ModelMapper _modelMapper;
    MessageController(MessageService messageService, ModelMapper modelMapper){
        this._messageService = messageService;
        this._modelMapper = modelMapper;
    };

    @GetMapping(path = "")
    public List<MessageEntity> getMessages(){
        return this._messageService.getAllMessages();
    }

    @GetMapping(path="/undelivered")
    public ResponseEntity<ResponseBody<List<ChatMessageDTO>>> getUndeliveredMessages(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<MessageEntity> undeliveredMessages =  this._messageService.getUndeliveredMessages(userDetails.getUsername());

        List<ChatMessageDTO> chatMessageDTOS = undeliveredMessages.stream()
                .map(messageEntity -> {
                    ChatMessageDTO chatMessageDTO = _modelMapper.map(messageEntity, ChatMessageDTO.class);
                    chatMessageDTO.setSenderId(messageEntity.getSender().getId().toString());
                    chatMessageDTO.setReceiverId(messageEntity.getReceiver().getId().toString());
                    chatMessageDTO.setTimestamp(messageEntity.getTimestamp());
                    chatMessageDTO.setMessageId(messageEntity.getId().toString());
                    chatMessageDTO.setContent(messageEntity.getContent());

                    return chatMessageDTO;
                })
                .toList();
        ResponseBody<List<ChatMessageDTO>> responseBody = ResponseBody.success(chatMessageDTOS);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping(path = "/latest-status")
    public ResponseEntity<ResponseBody<List<MessageUpdateDTO>>> getMessagesLatestStatus(@RequestBody MessageIdsDTO body){
        List<String> messageIds = body.getMessageIds();
        _logger.info("Getting latest status for messages: " + body.getMessageIds());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MessageEntity> messages = this._messageService.getMessagesLatestStatus(userDetails.getUsername(), messageIds);
        List<MessageEntity> readMessages = messages.stream()
                .filter(message -> message.getStatus().equals(MessageStatusEnum.READ))
                .toList();
        List<Long> readMessageIds = readMessages.stream()
                .map(MessageEntity::getId)
                .toList();
        List<MessageUpdateDTO> messageUpdateDTOS = messages.stream()
                .map(messageEntity -> {
                    MessageUpdateDTO messageUpdateDTO = new MessageUpdateDTO();
                    messageUpdateDTO.setMessageId(messageEntity.getId().toString());
                    messageUpdateDTO.setMessageClientId(messageEntity.getMessageClientId());
                    messageUpdateDTO.setStatus(messageEntity.getStatus());
                    return messageUpdateDTO;
                })
                .toList();

//        filter out read messages and delete them as it is not required anymore

        this._messageService.deleteMessages(readMessageIds);
        ResponseBody<List<MessageUpdateDTO>> responseBody = ResponseBody.success(messageUpdateDTOS);
        return ResponseEntity.ok(responseBody);
    }

}
