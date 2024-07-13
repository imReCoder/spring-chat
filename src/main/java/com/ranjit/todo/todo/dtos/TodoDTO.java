package com.ranjit.todo.todo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    private Long id;
    private String title;
    private String description;
    private Boolean completed;
}