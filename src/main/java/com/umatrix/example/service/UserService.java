package com.umatrix.example.service;


import com.umatrix.example.configs.security.service.JWTService;
import com.umatrix.example.models.Users;
import com.umatrix.example.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12) ;

    public Users register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(Users user) {
        Authentication authenticate
                = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        }
        return null;
    }

    public boolean checkExistence(String username){
        return userRepo.existsByUsername(username);
    }

}
