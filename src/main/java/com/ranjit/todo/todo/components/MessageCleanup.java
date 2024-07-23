package com.ranjit.todo.todo.components;

import com.ranjit.todo.todo.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MessageCleanup {

    private final Logger _logger = LoggerFactory.getLogger(MessageCleanup.class);
    private final MessageService messageService;

    @Value("${message.cleanup.interval}")
    private long cleanupIntervalInMs = 5000;

    public MessageCleanup(MessageService messageService){
        this.messageService = messageService;
    }

    @Scheduled(fixedRate = 5000,timeUnit = TimeUnit.MILLISECONDS)
    public void cleanupMessages(){
        _logger.info("Cleaning up messages triggered...");
        messageService.deleteMessagesByStatus();
        _logger.info("Cleaning up messages done...");
    }
}
