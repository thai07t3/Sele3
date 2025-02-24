package tests.content;

import models.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ContentTesting {
    @Test(description = "Content testing", groups = {"smoke", "regression"})
    public void ContentValidate() throws IOException, InterruptedException {
        // 1. Download html from website
        String htmlContent = downloadHtml();

        // 2. Parse HTML to get data of products
        List<Product> htmlProducts = parseHtml(htmlContent);

        // 3. Read data from exel file to get data of products
        List<Product> excelProducts = readExcel("src/test/resources/data/Content_Testing_LeapFrog-games.xlsx");

        // 4. Validate data
        validateData(htmlProducts, excelProducts);
    }

    private static String downloadHtml() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://store.leapfrog.com/en-us/apps/c?p=1&platforms=197&product_list_dir=asc&product_list_order=name"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private static List<Product> parseHtml(String html) {
        List<Product> products = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        Elements productElements = doc.selectXpath("//div[@class='row product-row']//div[@class='catalog-product']");

        for (Element product : productElements) {
            String title = product.select("p[class='heading '], p[class='heading clamped']").text();
            String age = product.select("p.ageDisplay").text();
            String price = product.select("p.prices").text();
//            System.out.println(title + " - " + age + " - " + price);

            products.add(new Product(title, age, price));
        }
        return products;
    }

    private static List<Product> readExcel(String filePath) throws IOException {
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

    private static void validateData(List<Product> htmlProducts, List<Product> excelProducts) {
        for (Product excelProduct : excelProducts) {
            boolean isMatch = htmlProducts.stream().anyMatch(htmlProduct ->
                    normalizeTitle(htmlProduct.title).contains(normalizeTitle(excelProduct.title)) &&
                            normalizeAge(htmlProduct.age).equals(normalizeAge(excelProduct.age)) &&
                            normalizePrice(htmlProduct.price) == normalizePrice(excelProduct.price));

            System.out.println((isMatch ? "✓" : "✗") + " " + excelProduct.title);
        }
    }

    // Methods to normalize data
    private static String normalizeTitle(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9àâäéèêëîïôöùûüÿç]", "");
    }

    private static String normalizeAge(String age) {
        return age.replaceAll("[^0-9-]", "");
    }

    private static double normalizePrice(String price) {
        return Double.parseDouble(price.replaceAll("[^0-9.]", ""));
    }
}