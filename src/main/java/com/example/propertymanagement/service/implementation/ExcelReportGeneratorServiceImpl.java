package com.example.propertymanagement.service.implementation;


import com.example.propertymanagement.model.Property;
import com.example.propertymanagement.model.dto.UserDto;
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
@Qualifier("UserReport")
@Service
@RequiredArgsConstructor
public class ExcelReportGeneratorServiceImpl implements ExcelReportGeneratorService {
    private final UserServiceImpl userService;
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public void writeHeader() {
        sheet = workbook.createSheet("OwnersList");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "id", style);
        createCell(row, 1, "email", style);
        createCell(row, 2, "role", style);
        createCell(row, 3, "properties", style);
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
        List<UserDto> userList = userService.getAllUsers();
        for (UserDto user: userList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, user.id, style);
            createCell(row, columnCount++, user.email, style);
            createCell(row, columnCount++, user.role.toString(), style);

            Set<Property> propertySet = user.properties;
            for (Property property : propertySet) {
                createCell(row, columnCount++, property.getAddress(), style);
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