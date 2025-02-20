package io.github.bonigarcia;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String image;
    private String description;
    private double price;
    private String title;
    private String category;

    public Product() {
    }

    public Product(int id, String image, String description, double price, String title, String category) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.price = price;
        this.title = title;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

}