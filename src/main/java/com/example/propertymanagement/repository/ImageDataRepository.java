package com.example.propertymanagement.repository;

import com.example.propertymanagement.model.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageDataRepository extends JpaRepository<ImageData,Long> {

    List<ImageData> findImageDataByPropertyAddress(String property_address);

    @Transactional
    void deleteImageDataByPropertyAddress(String property_address);
}
