package tests.content;

import com.codeborne.selenide.Configuration;
import models.Product;
import org.testng.annotations.Test;
import pages.content.LeapFrog;
import tests.BaseTest;
import utils.Constants;

import java.io.IOException;
import java.util.List;

import static helpers.ExelHelper.readExcelToProduct;

public class ContentTest extends BaseTest {
    LeapFrog leapFrog = new LeapFrog();

    @Test(description = "Content testing", groups = {"smoke", "regression"})
    public void ContentValidate() throws IOException, InterruptedException {
        // 1. Download html from website
        String htmlContent = leapFrog.downloadHtml(Configuration.baseUrl);

        // 2. Parse HTML to get data of products
        List<Product> htmlProducts = leapFrog.parseHtml(htmlContent);

        // 3. Read data from exel file to get data of products
        List<Product> excelProducts = readExcelToProduct(Constants.LEAPFROG_EXEL_PATH);

        // 4. Validate data
        leapFrog.shouldDataBeValidated(htmlProducts, excelProducts);
    }
}