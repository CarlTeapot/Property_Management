package com.example.propertymanagement.service;

import com.example.propertymanagement.model.Property;
import com.example.propertymanagement.model.User;
import com.example.propertymanagement.model.UserRole;
import com.example.propertymanagement.model.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
     void addUser(User user);

     UserDto getUser(String email);

     User getUserInformation(String email);

     void checkUser(String email);

     List<UserDto> getAllUsers();

     void addPropertyToUser(String email, String address);

     void removePropertyFromUser(String email, String address);

     void removePropertyFromAllUsers(List<User> users, String address);

     void changePasswordAsAdmin(String email, String password);

     void changePassword(@NonNull HttpServletRequest request,
                         String password);

     List<Property> getUserProperties(String email);

     void changeRole(String email, UserRole role);

     void changeBudget(@NonNull HttpServletRequest request, Long budget);

     void changeBudgetAsAdmin(String email,Long budget);
}

