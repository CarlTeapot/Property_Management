package com.example.propertymanagement.repository;

import com.example.propertymanagement.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    Optional<Property> findPropertyByAddress(String address);


    List<Property> findPropertiesByUsersId(Long userId);

    List<Property> findPropertiesByPrice(Long price);

}
