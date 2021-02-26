package com.example.Test.controller;

import com.example.Test.entity.TodoEntity;
import com.example.Test.exeption.PermissionDeniedException;
import com.example.Test.security.AppUserDetails;
import com.example.Test.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    private AppUserDetails userPrincipal;

    @PostMapping
    public ResponseEntity createTodo(@RequestBody TodoEntity todo,
                                     @RequestParam Long userId, Authentication authentication) {
        if (checkUser(userId, authentication)) {
            try {
                return ResponseEntity.ok(todoService.createTodo(todo, userId));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Ошибочка вышла :(");
            }
        } else {
            return ResponseEntity.badRequest().body("В доступе отказано");
        }
    }

    @PatchMapping
    public ResponseEntity changeTodo(@RequestBody String title,
                                     @RequestParam Long todoId, Authentication authentication) {

        try {
            return ResponseEntity.ok(todoService.changeTodo(title, todoId,authentication));
        } catch (PermissionDeniedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибочка вышла :(");
        }
    }

    @PutMapping
    public ResponseEntity completeTodo(@RequestParam Long todoId, Authentication authentication) {

        try {
            return ResponseEntity.ok(todoService.completeTodo(todoId, authentication));
        } catch (PermissionDeniedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибочка вышла :(");
        }
    }

    @DeleteMapping
    public ResponseEntity deleteTodo(@RequestParam Long todoId,
                                     @RequestParam Long userId, Authentication authentication) {
        if (checkUser(userId, authentication)) {
            try {
                return ResponseEntity.ok(todoService.deleteTodo(todoId));
            } catch (PermissionDeniedException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("ERROR");
            }
        } else {
            return ResponseEntity.badRequest().body("В доступе отказано");
        }
    }

    public boolean checkUser(Long userId, Authentication authentication) {
        userPrincipal = (AppUserDetails) authentication.getPrincipal();
        return userPrincipal.getId().equals(userId) || userPrincipal.getRole().equals("ROLE_ADMIN");
    }

}
