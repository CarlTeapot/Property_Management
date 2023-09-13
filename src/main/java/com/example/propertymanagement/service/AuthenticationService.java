package com.example.propertymanagement.service;

import com.example.propertymanagement.model.User;
import com.example.propertymanagement.model.UserAuthenticationRequest;
import com.example.propertymanagement.service.implementation.AuthenticationResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
     AuthenticationResponse addUser(User user);

     AuthenticationResponse authenticate(UserAuthenticationRequest request);
}

