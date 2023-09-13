package com.example.propertymanagement.controller;

import com.example.propertymanagement.model.User;
import com.example.propertymanagement.model.UserRole;
import com.example.propertymanagement.model.dto.UserDto;
import com.example.propertymanagement.service.UserService;
import com.example.propertymanagement.service.implementation.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("get-user/{email}")
    @Cacheable(value = "userCache")
    public UserDto getUser(@PathVariable("email") String email) {
        return userService.getUser(email);
    }

    @GetMapping("get-user-information/{email}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserInformation(@PathVariable("email") String email) {
        return userService.getUserInformation(email);
    }

    @GetMapping("get-all-users")
    @Cacheable(value = "usersCache")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("change-password-as-admin/{email}/{password}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void changePasswordAsAdmin(@PathVariable("email") String email,
                                      @PathVariable("password") String password) {
        userService.changePasswordAsAdmin(email, password);
    }

    @PutMapping("change-password/{email}/{password}")
    public void changePassword(@NonNull HttpServletRequest request,
                               @PathVariable("email") String email,
                               @PathVariable("password") String password) {
        userService.changePassword(request, password);
    }

    @PutMapping("change-role/{email}/{role}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void changeRole(@PathVariable("email") String email,
                           @PathVariable("role") UserRole role) {
        userService.changeRole(email, role);
    }

    @PutMapping("change-budget/{budget}")
    public void changeBudget(@NonNull HttpServletRequest request,
                             @PathVariable("budget") Long budget) {
        userService.changeBudget(request, budget);
    }

    @PutMapping("change-budget-as-admin/{email}/{budget}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void changeBudgetAsAdmin(@PathVariable("email") String email,
                                    @PathVariable("budget") Long budget) {
        userService.changeBudgetAsAdmin(email, budget);

    }
}
