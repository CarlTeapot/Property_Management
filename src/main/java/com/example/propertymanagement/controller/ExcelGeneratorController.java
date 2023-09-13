package com.example.propertymanagement.controller;

import com.example.propertymanagement.service.ExcelReportGeneratorService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ExcelGeneratorController {
    @Autowired
    @Qualifier("UserReport")
    private ExcelReportGeneratorService service;

    @Autowired
    @Qualifier("PropertyReport")
    private  ExcelReportGeneratorService service2;

    @GetMapping("/generate-user-report")
    public void generateUsersList(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        service.generateExcelFile(response);
    }
    @GetMapping("/generate-properties-report")
    public void generatePropertiesList(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=properties_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        service2.generateExcelFile(response);
    }
}

