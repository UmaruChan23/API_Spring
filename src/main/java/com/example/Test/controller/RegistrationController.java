package com.example.Test.controller;

import com.example.Test.entity.UserEntity;
import com.example.Test.exeption.UserAlreadyExistException;
import com.example.Test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reg")
public class RegistrationController {

        @Autowired
        private UserService userService;

        @PostMapping
        public ResponseEntity registration(@RequestBody UserEntity user) {
            try {
                userService.registration(user);
                return ResponseEntity.ok("Пользователь создан!");
            } catch (UserAlreadyExistException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Ошибочка вышла :(");
            }
        }

}
