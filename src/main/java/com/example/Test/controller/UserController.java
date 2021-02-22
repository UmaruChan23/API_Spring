package com.example.Test.controller;

import com.example.Test.exeption.PermissionDeniedException;
import com.example.Test.exeption.UserNotFoundException;
import com.example.Test.security.AppUserDetails;
import com.example.Test.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private AppUserDetails userPrincipal;

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
    public ResponseEntity deleteUser(@RequestParam Long id, Authentication authentication) {
        userPrincipal = (AppUserDetails) authentication.getPrincipal();
        if (userPrincipal.getId().equals(id) || userPrincipal.getRole().equals("ROLE_ADMIN")) {
            try {
                return ResponseEntity.ok(userService.deleteUser(id));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Ошибочка вышла :(");
            }
        } else {
            return ResponseEntity.badRequest().body("В доступе отказано");
        }
    }


}


