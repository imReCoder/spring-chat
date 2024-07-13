package com.ranjit.todo.todo.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBody<T> {
    String status;
    String message;
    T data;

    public static <T> ResponseBody<T> success(T data){
        return new ResponseBody<>("success","Success",data);
    }

    public static <T> ResponseBody<T> error(String message){
        return new ResponseBody<>("error",message,null);
    }
}
