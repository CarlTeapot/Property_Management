package com.example.propertymanagement.service.implementation;

import com.example.propertymanagement.model.ImageData;
import com.example.propertymanagement.model.Property;
import com.example.propertymanagement.repository.ImageDataRepository;
import com.example.propertymanagement.repository.PropertyRepository;

import com.example.propertymanagement.service.ImageUploadService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@AllArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {
    private final Logger logger;
    private final ImageDataRepository imageDataRepository;
    private final PropertyRepository propertyRepository;

    public String uploadImage(MultipartFile file, String address) throws IOException {

        String FOLDER_PATH = "C:/Users/u/Desktop/Images/";
        String filePath = FOLDER_PATH + file.getOriginalFilename();

        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        checkProperty(address);

        ImageData imageData = imageDataRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .property(property.get())
                .filePath(filePath).build());

        file.transferTo(new File(filePath));


        Set<ImageData> images = null;
        images = property.get().getImages();
        images.add(imageData);
        property.get().setImages(images);
        propertyRepository.save(property.get());
        if (imageData != null)
            return "uploaded successfully";

        return null;
    }

    public List<ImageData> getImageDataByProperty(String address) {
        checkProperty(address);

        return imageDataRepository.findImageDataByPropertyAddress(address);
    }
    public void deleteImageDataByProperty(String address) {
        imageDataRepository.deleteImageDataByPropertyAddress(address);
    }


    public void checkProperty(String address) {
        Optional<Property> property = propertyRepository.findPropertyByAddress(address);
        boolean exists = property.isPresent();
        if (!exists)
            throw new IllegalStateException("properties does not exist");
        logger.trace("property checked");
    }

    public File createZipFile(String address) throws IOException {
        checkProperty(address);

        List<ImageData> imageDataList = getImageDataByProperty(address);
        File zipFile = File.createTempFile("images", ".zip");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            byte[] buffer = new byte[1024];
            for (ImageData data : imageDataList) {
                ZipEntry entry = new ZipEntry(data.getName());
                zipOutputStream.putNextEntry(entry);

                try (FileInputStream inputStream = new FileInputStream(data.getFilePath())) {
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }
                }

                zipOutputStream.closeEntry();
            }
        }
        return zipFile;
    }

    public ResponseEntity<FileSystemResource> downloadImagesAsZip(String address) throws IOException {
        File zipFile = createZipFile(address);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "images.zip");

        return new ResponseEntity<>(new FileSystemResource(zipFile), headers,
                org.springframework.http.HttpStatus.OK);
    }
}


