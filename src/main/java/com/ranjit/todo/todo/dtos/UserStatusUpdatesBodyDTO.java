package com.ranjit.todo.todo.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UserStatusUpdatesBodyDTO {
    List<String> userIds;
}
