package com.example.Test.service;

import com.example.Test.entity.TodoEntity;
import com.example.Test.entity.UserEntity;
import com.example.Test.exeption.PermissionDeniedException;
import com.example.Test.model.Todo;
import com.example.Test.repository.TodoRepo;
import com.example.Test.repository.UserRepo;
import com.example.Test.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    public Todo changeTodo(String todoText, Long todoId, Authentication authentication) throws PermissionDeniedException {
        TodoEntity todo = todoRepo.findById(todoId).get();
        if (checkUser(todo, authentication)) {
            todo.setTitle(todoText);
            return Todo.toModel(todoRepo.save(todo));
        } else {
            throw new PermissionDeniedException("В доступе отказано");
        }
    }

    public Todo completeTodo(Long id, Authentication authentication) throws PermissionDeniedException {
        TodoEntity todo = todoRepo.findById(id).get();
        if (checkUser(todo, authentication)) {
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

    public boolean checkUser(TodoEntity todo, Authentication authentication) {
        AppUserDetails userPrincipal = (AppUserDetails) authentication.getPrincipal();
        return (todo.getUser().getId().equals(userPrincipal.getId())) || userPrincipal.getRole().equals("ROLE_ADMIN");
    }
}
