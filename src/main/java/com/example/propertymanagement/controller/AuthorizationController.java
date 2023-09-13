package com.example.propertymanagement.controller;

import com.example.propertymanagement.model.User;
import com.example.propertymanagement.model.UserAuthenticationRequest;
import com.example.propertymanagement.service.AuthenticationService;
import com.example.propertymanagement.service.implementation.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authorization")
public class AuthorizationController {
    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(service.addUser(user));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody UserAuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
