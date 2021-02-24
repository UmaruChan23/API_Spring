package com.example.Test.service;

import com.example.Test.entity.TodoEntity;
import com.example.Test.entity.UserEntity;
import com.example.Test.exeption.PermissionDeniedException;
import com.example.Test.model.Todo;
import com.example.Test.repository.TodoRepo;
import com.example.Test.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    @Autowired
    private TodoRepo todoRepo;

    @Autowired
    private UserRepo userRepo;

    public Todo createTodo(TodoEntity todo, Long userId) throws PermissionDeniedException {
        UserEntity user = userRepo.findById(userId).get();
        todo.setUser(user);
        return Todo.toModel(todoRepo.save(todo));
    }

    public Todo changeTodo(String todoText, Long todoId){
        TodoEntity todo = todoRepo.findById(todoId).get();
        todo.setTitle(todoText);
        return Todo.toModel(todoRepo.save(todo));
    }

    public Todo completeTodo(Long id) throws PermissionDeniedException {
        TodoEntity todo = todoRepo.findById(id).get();
        todo.setCompleted(!todo.getCompleted());
        return Todo.toModel(todoRepo.save(todo));
    }

    public Long deleteTodo(Long id) throws PermissionDeniedException {
        todoRepo.deleteById(id);
        return id;
    }
}
