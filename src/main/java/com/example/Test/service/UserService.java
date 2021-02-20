package com.example.Test.service;

import com.example.Test.entity.UserEntity;
import com.example.Test.exeption.UserAlreadyExistException;
import com.example.Test.exeption.UserNotFoundException;
import com.example.Test.model.User;
import com.example.Test.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public void registration(UserEntity user) throws UserAlreadyExistException {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistException("Пользователь уже существует");
        }
        userRepo.save(user);
    }

    public User loadUserByUsername(String username) throws UserNotFoundException {
        UserEntity user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return User.toModel(user);
    }

    public Long deleteUser(Long id) {
        userRepo.deleteById(id);
        return id;
    }
}
