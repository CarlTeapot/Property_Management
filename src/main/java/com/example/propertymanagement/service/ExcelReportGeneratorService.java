package com.example.propertymanagement.service;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ExcelReportGeneratorService {
    void writeHeader();

    void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style);

    void write();

    void generateExcelFile(HttpServletResponse response) throws IOException;


}
