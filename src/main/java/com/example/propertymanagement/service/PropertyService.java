package com.example.propertymanagement.service;

import com.example.propertymanagement.model.Property;
import com.example.propertymanagement.model.User;
import com.example.propertymanagement.model.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public interface PropertyService {
    public void addProperty(Property property);

    public void checkProperty(String address);

    public void changePropertyPrice(HttpServletRequest request,String address, Long price);

    public List<Property> getAllProperties();

    public List<UserDto> getPropertyOwnerDtos(String address);

    public List<User> getPropertyOwners(String address);

    public void deleteProperty(@NonNull HttpServletRequest request, String address);

    public void approveDeletion(String email, String address);

    public void removeDeletion(String email, String address);

    public void removeDeletionFromAllUsers(List<User> users, String address);

    public void checkUser(String email);

    public void requestToPurchaseProperty(@NonNull HttpServletRequest request,
                                          String address, Long offer);
    public void approvePurchase(@NonNull HttpServletRequest request,
                                String address, String email);

}
