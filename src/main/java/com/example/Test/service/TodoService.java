package com.example.Test.service;

import com.example.Test.entity.TodoEntity;
import com.example.Test.entity.UserEntity;
import com.example.Test.exeption.PermissionDeniedException;
import com.example.Test.model.Todo;
import com.example.Test.repository.TodoRepo;
import com.example.Test.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    @Autowired
    private TodoRepo todoRepo;

    @Autowired
    private UserRepo userRepo;

    public Todo createTodo(TodoEntity todo, Long userId) {
        UserEntity user = userRepo.findById(userId).get();
        todo.setUser(user);
        return Todo.toModel(todoRepo.save(todo));
    }

    public Todo changeTodo(String todoText, Long todoId, Long userId) throws PermissionDeniedException {
        TodoEntity todo = todoRepo.findById(todoId).get();
        if (checkUser(todo, userId)) {
            todo.setTitle(todoText);
            return Todo.toModel(todoRepo.save(todo));
        } else {
            throw new PermissionDeniedException("В доступе отказано");
        }
    }

    public Todo completeTodo(Long id, Long userId) throws PermissionDeniedException {
        TodoEntity todo = todoRepo.findById(id).get();
        if (checkUser(todo, userId)) {
            todo.setCompleted(!todo.getCompleted());
            return Todo.toModel(todoRepo.save(todo));
        } else {
            throw new PermissionDeniedException("В доступе отказано");
        }
    }

    public Long deleteTodo(Long id) throws PermissionDeniedException {
        todoRepo.deleteById(id);
        return id;
    }

    public boolean checkUser(TodoEntity todo, Long userId) {
        return (todo.getUser().getId().equals(userId)) || todo.getUser().getRole().equals("ROLE_ADMIN");
    }
}
