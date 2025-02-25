package pages.content;

import models.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pages.BasePage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LeapFrog extends BasePage {

    public String downloadHtml(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public List<Product> parseHtml(String html) {
        List<Product> products = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        Elements productElements = doc.selectXpath("//div[@class='row product-row']//div[@class='catalog-product']");

        for (Element product : productElements) {
            String title = product.select("p[class='heading '], p[class='heading clamped']").text();
            String age = product.select("p.ageDisplay").text();
            String price = product.select("p.prices").text();

            products.add(new Product(title, age, price));
        }
        return products;
    }

    public boolean validateData(List<Product> htmlProducts, List<Product> excelProducts) {
        Set<Boolean> results = new HashSet<>();
        for (Product excelProduct : excelProducts) {
            boolean isMatch = htmlProducts.stream().anyMatch(htmlProduct ->
                    normalizeTitle(htmlProduct.title).contains(normalizeTitle(excelProduct.title)) &&
                            normalizeAge(htmlProduct.age).equals(normalizeAge(excelProduct.age)) &&
                            normalizePrice(htmlProduct.price) == normalizePrice(excelProduct.price));

            System.out.println((isMatch ? "✓" : "✗") + " " + excelProduct.title +
                    " - " + excelProduct.age + " - " + excelProduct.price);
            results.add(isMatch);
        }
        return !results.contains(false);
    }

    private String normalizeTitle(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9àâäéèêëîïôöùûüÿç]", "");
    }

    private String normalizeAge(String age) {
        return age.replaceAll("[^0-9-]", "");
    }

    private double normalizePrice(String price) {
        return Double.parseDouble(price.replaceAll("[^0-9.]", ""));
    }
}
