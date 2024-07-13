package com.ranjit.todo.todo.controllers;

import com.ranjit.todo.todo.dtos.TodoDTO;
import com.ranjit.todo.todo.services.TodoService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "todo")
public class TodoController {

    final TodoService  _todoService;
    public TodoController(TodoService _todoService){
        this._todoService = _todoService;
    }
    @GetMapping(path = "")
    public String ping() {
        return "Server is working!!";
    }

    @PostMapping(path = "/add")
    public TodoDTO addTodo(@RequestBody TodoDTO todoDto) {
       return _todoService.createTodo(todoDto);
    }

    @GetMapping(path="/all")
    public List<TodoDTO> getAllTodos(@RequestAttribute("user") UserDetails user){
        System.out.println("User: "+user);
        return _todoService.getAllTodos();
    }
}