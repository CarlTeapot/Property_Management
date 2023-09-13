package com.example.propertymanagement.model;

import org.springframework.security.core.GrantedAuthority;

 public enum UserRole implements GrantedAuthority {
        SUPPORT,
        PROPERTY_ADMIN,
        ADMIN,
        USER;

        @Override
        public String getAuthority() {
            return name();
        }
    }
