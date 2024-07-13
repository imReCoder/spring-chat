package com.ranjit.todo.todo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    Long id;
    String name;
    String email;
    String password;
    String profileImage;
    String provider;
}
