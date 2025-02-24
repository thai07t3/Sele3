package models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
    public String title;
    public String age;
    public String price;

    public Product(String title, String age, String price) {
        this.title = title.trim();
        this.age = age.trim();
        this.price = price.trim();
    }
}
