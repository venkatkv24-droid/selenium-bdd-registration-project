package com.qa.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads user registration rows from the Excel sheet (UserRegistrationData.xlsx).
 * Sheet layout expected:
 * TestCaseId | FirstName | LastName | Email | Password
 */
public class ExcelUtils {

    private final String filePath;
    private final String sheetName;

    public ExcelUtils(String filePath, String sheetName) {
        this.filePath = filePath;
        this.sheetName = sheetName;
    }

    /**
     * Returns the row matching the given TestCaseId as a column-name -> value map.
     * Example: getUserData("User1") -> {FirstName=Alice, LastName=Johnson, Email=..., Password=...}
     */
    public Map<String, String> getUserData(String testCaseId) {
        Map<String, String> data = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            Row headerRow = sheet.getRow(0);
            int totalColumns = headerRow.getLastCellNum();
            int totalRows = sheet.getLastRowNum();

            for (int rowIndex = 1; rowIndex <= totalRows; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                String currentTestCaseId = getCellValueAsString(row.getCell(0));
                if (currentTestCaseId.equalsIgnoreCase(testCaseId)) {
                    for (int col = 0; col < totalColumns; col++) {
                        String columnName = getCellValueAsString(headerRow.getCell(col));
                        String cellValue = getCellValueAsString(row.getCell(col));
                        data.put(columnName, cellValue);
                    }
                    return data;
                }
            }

            throw new RuntimeException("TestCaseId '" + testCaseId + "' not found in sheet '" + sheetName + "'");

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file at " + filePath, e);
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double numValue = cell.getNumericCellValue();
                if (numValue == Math.floor(numValue)) {
                    return String.valueOf((long) numValue);
                }
                return String.valueOf(numValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
