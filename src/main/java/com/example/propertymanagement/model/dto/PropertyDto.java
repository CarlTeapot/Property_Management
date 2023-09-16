package com.example.propertymanagement.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.example.propertymanagement.model.Property} entity
 */
@Data
@Builder
public class PropertyDto implements Serializable {
    private final Set<UserDto> users;
    private final Double size;
    private final String address;
    private final Long price;
}