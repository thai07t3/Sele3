package helpers;

import models.content.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExelHelper {
    public static List<Product> readExcelToProduct(String filePath) throws IOException {
        List<Product> products = new ArrayList<>();
        FileInputStream file = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(1);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Bỏ qua header

            String title = getCellValue(row.getCell(0));
            String age = getCellValue(row.getCell(1));
            String price = getCellValue(row.getCell(2));

            products.add(new Product(title, age, price));
        }
        workbook.close();
        return products;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            default -> "";
        };
    }
}
