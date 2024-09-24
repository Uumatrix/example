package com.umatrix.example.controllers;

import com.umatrix.example.exceptionHandling.CustomExceptions.UserAlreadyExists;
import com.umatrix.example.models.Users;
import com.umatrix.example.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users register(@Valid @RequestBody Users user) {
        if(userService.checkExistence(user.getUsername())){
            throw new UserAlreadyExists();
        }
        return userService.register(user);
    }

    @PostMapping("/login")
    public String logIn(@Valid @RequestBody Users user) {
        return userService.verify(user);
    }

    @Hidden
    @GetMapping("/test")
    public String test(){
        return "test";
    }
}



