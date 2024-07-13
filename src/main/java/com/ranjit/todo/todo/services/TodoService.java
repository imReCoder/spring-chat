package com.ranjit.todo.todo.services;

import com.ranjit.todo.todo.dtos.TodoDTO;
import com.ranjit.todo.todo.entities.TodoEntity;
import com.ranjit.todo.todo.repositories.TodoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {

//    @Autowired
//    TodoRepository todoRepository;

    final TodoRepository _todoRepository;
    final ModelMapper _modelMapper;
    public TodoService(TodoRepository _todoRepository,ModelMapper _modelMapper){
        this._todoRepository = _todoRepository;
        this._modelMapper = _modelMapper;

    }

    public List<TodoDTO> getAllTodos(){
         return _todoRepository
                 .findAll()
                 .stream()
                 .map(todoEntity -> _modelMapper.map(todoEntity, TodoDTO.class))
                 .collect(Collectors.toList());
    }

    public TodoDTO getTodoById(Long id){
        TodoEntity todoEntity = _todoRepository.getReferenceById(id);
        return _modelMapper.map(todoEntity,TodoDTO.class);
    }

    public TodoDTO createTodo(TodoDTO todoDto){
        TodoEntity todoEntity = _modelMapper.map(todoDto,TodoEntity.class);
        return _modelMapper.map(_todoRepository.save(todoEntity),TodoDTO.class);
    };
}
