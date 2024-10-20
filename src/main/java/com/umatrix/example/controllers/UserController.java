package com.umatrix.example.controllers;

import com.umatrix.example.dto.UserDto;
import com.umatrix.example.exceptionHandling.CustomExceptions.UserAlreadyExists;
import com.umatrix.example.mapstruct.UserMapper;
import com.umatrix.example.models.Users;
import com.umatrix.example.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "registers a user", description = "creates user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @PostMapping("/register")
    public Users register(@Valid @RequestBody UserDto userDto) {
        Users user = UserMapper.INSTANCE.toUser(userDto);
        if(userService.checkExistence(user.getUsername())){
            throw new UserAlreadyExists();
        }
        return userService.register(user);
    }

    @Operation(summary = "log in", description = "log in to account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "user not found")
    })
    @PostMapping("/login")
    public String logIn(@Valid @RequestBody Users user) {
        return userService.verify(user);
    }

}



