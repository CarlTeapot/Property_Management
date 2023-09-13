package com.example.propertymanagement.controller;

import com.example.propertymanagement.model.UserRole;
import com.example.propertymanagement.service.UserService;
import com.example.propertymanagement.service.implementation.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("free")
public class FreeController {
    private final UserService userService;
    @PutMapping("change-role/{email}/{role}")
    public void changeRole( @PathVariable("email") String email,
                            @PathVariable("role") UserRole role) {
        userService.changeRole( email, role);
    }
}
