package com.example.Test.service;

import com.example.Test.entity.TodoEntity;
import com.example.Test.entity.UserEntity;
import com.example.Test.exeption.PermissionDeniedException;
import com.example.Test.model.Todo;
import com.example.Test.repository.TodoRepo;
import com.example.Test.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TodoService {
    @Autowired
    private TodoRepo todoRepo;

    @Autowired
    private UserRepo userRepo;

    public Todo createTodo(TodoEntity todo, Long userId) throws PermissionDeniedException {
        if (inspectUserById(userId)) {
            UserEntity user = userRepo.findById(userId).get();
            todo.setUser(user);
            return Todo.toModel(todoRepo.save(todo));
        } else {
            throw new PermissionDeniedException("В доступе отказано");
        }

    }

    public Todo completeTodo(Long id) throws PermissionDeniedException {
        if (inspectUserById(id)) {
            TodoEntity todo = todoRepo.findById(id).get();
            todo.setCompleted(!todo.getCompleted());
            return Todo.toModel(todoRepo.save(todo));
        } else {
            throw new PermissionDeniedException("В доступе отказано");
        }

    }

    public Long deleteTodo(Long id) throws PermissionDeniedException {
        if (inspectUserById(id)) {
            todoRepo.deleteById(id);
            return id;
        } else {
            throw new PermissionDeniedException("В доступе отказано");
        }
    }

    public boolean inspectUserById(Long id) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Optional<UserEntity> user = userRepo.findById(id);
        return principal.getPassword().equals(user.get().getPassword()) || user.get().getRole().equals("ROLE_ADMIN");
    }
}
