package com.ranjit.todo.todo.services;

import com.ranjit.todo.todo.dtos.UserStatusUpdate;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserOnlineStatus {
    @Getter
    private final HashMap<String,Boolean> onlineUsers = new HashMap<String, Boolean>();
    private final SimpMessagingTemplate simpMessagingTemplate;
    private Logger _logger = LoggerFactory.getLogger(UserOnlineStatus.class);

    public UserOnlineStatus(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void setUserOnline(String userId){
        onlineUsers.put(userId,true);
        notifyUserStatusChange(userId,true);
    }

    public void setUserOffline(String userId){
        this.removeUser(userId);
        notifyUserStatusChange(userId,false);
    }

    public boolean isUserOnline(String userId){
        Boolean isOnline =  onlineUsers.get(userId);
        if (isOnline == null){
            return false;
        }
        return isOnline;
    }

    private void removeUser(String userId){
        onlineUsers.remove(userId);
    }

    private void notifyUserStatusChange(String userId, boolean status){
        String topic = "/topic/user.status/"+userId;
        _logger.info("Sending user status to: "+topic);
        UserStatusUpdate userStatusUpdate = new UserStatusUpdate();
        userStatusUpdate.setUserId(userId);
        userStatusUpdate.setStatus(status);
        simpMessagingTemplate.convertAndSend("/topic/user.status/"+userId,userStatusUpdate);
    }


}
