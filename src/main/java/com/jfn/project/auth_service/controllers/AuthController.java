package com.jfn.project.auth_service.controllers;

import com.jfn.project.auth_service.beans.Utilisateur;
import com.jfn.project.auth_service.dto.LoginRequest;
import com.jfn.project.auth_service.dto.LoginResponse;
import com.jfn.project.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Utilisateur> register(@RequestBody Utilisateur utilisateur) {
        return ResponseEntity.status(201).body(authService.register(utilisateur));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}