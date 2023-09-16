package com.example.propertymanagement.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.example.propertymanagement.model.ImageData} entity
 */
@Data
@Builder
public class ImageDataDto implements Serializable {
    private final String name;
    private final PropertyDto property;
}