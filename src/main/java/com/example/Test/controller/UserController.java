package com.example.Test.controller;

import com.example.Test.entity.UserEntity;
import com.example.Test.exeption.UserAlreadyExistException;
import com.example.Test.exeption.UserNotFoundException;
import com.example.Test.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity getOneUser(@RequestParam String username) {
        try {
            return ResponseEntity.ok(userService.findUserByUsername(username));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибочка вышла :(");
        }
    }


    @DeleteMapping
    public ResponseEntity deleteUser(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(userService.deleteUser(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибочка вышла :(");
        }
    }
}


