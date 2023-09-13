package com.example.propertymanagement.controller;

import com.example.propertymanagement.model.ImageData;
import com.example.propertymanagement.service.ImageUploadService;
import com.example.propertymanagement.service.implementation.ImageUploadServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/pictures")
public class ImageUploadController {
    private final ImageUploadService service;

    @PostMapping("add_pictures/{address}")
    public ResponseEntity<?> uploadPicture(
            @PathVariable("address") String address,
            @RequestParam("image") MultipartFile file)
            throws IOException {
        String uploadImage = service.uploadImage(file, address);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }

    @GetMapping("getImageData/{address}")
    public List<ImageData> getImageDataByProperty(
            @PathVariable("address") String address) {
        return service.getImageDataByProperty(address);
    }

    @GetMapping(value = "downloadImages/{address}", produces = "application/zip")
    public ResponseEntity<FileSystemResource> downloadImages(@PathVariable("address") String address) throws IOException {
        return service.downloadImagesAsZip(address);
    }
    @DeleteMapping("/delete/{address}")
    public void deleteImageDataByProperty(@PathVariable("address") String address) {
        service.deleteImageDataByProperty(address);
    }
}
