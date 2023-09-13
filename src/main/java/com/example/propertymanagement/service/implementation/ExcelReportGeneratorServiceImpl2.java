package com.example.propertymanagement.service.implementation;

import com.example.propertymanagement.model.Property;
import com.example.propertymanagement.model.User;
import com.example.propertymanagement.service.ExcelReportGeneratorService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
@Qualifier("PropertyReport")
@Service
@RequiredArgsConstructor
public class ExcelReportGeneratorServiceImpl2 implements ExcelReportGeneratorService {
    private final PropertyServiceImpl propertyService;
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public void writeHeader() {
        sheet = workbook.createSheet("PropertyList");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "id", style);
        createCell(row, 1, "address", style);
        createCell(row, 2, "price", style);
        createCell(row, 3, "size", style);
        createCell(row, 4, "owners", style);
    }
    public void createCell(Row row, int columnCount,
                           Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer)
            cell.setCellValue((Integer) valueOfCell);
        else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        }
        else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    public void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        List<Property> properties = propertyService.getAllProperties();
        for (Property property : properties) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, property.getId(), style);
            createCell(row, columnCount++, property.getAddress(), style);
            createCell(row, columnCount++, property.getPrice(), style);
            createCell(row, columnCount++, property.getSize(), style);
            Set<User> users = property.getUsers();
            for (User user : users) {
                createCell(row, columnCount++, user.getEmail(), style);
            }
        }
    }

    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
