package com.example.propertymanagement.model.dto;

import com.example.propertymanagement.model.Property;
import com.example.propertymanagement.model.User;
import com.example.propertymanagement.model.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserDto  {
    @JsonProperty("id")
    public Long id;
    @JsonProperty("email")
    public String email;
    @JsonProperty("role")
    public UserRole role;

    public Set<Property> properties;
    public UserDto(User user) {
        this.id = user.getId();
        email = user.getEmail();
        role = user.getRole();
        properties = user.getProperties();
    }
}
