package com.example.propertymanagement.service;

import com.example.propertymanagement.model.ImageData;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public interface ImageUploadService {
    String uploadImage(MultipartFile file, String address) throws IOException;

    List<ImageData> getImageDataByProperty(String address);

    void deleteImageDataByProperty(String address);


    void checkProperty(String address);

    File createZipFile(String address) throws IOException;

    ResponseEntity<FileSystemResource> downloadImagesAsZip(String address)
     throws IOException;
}
