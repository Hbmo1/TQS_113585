package io.github.bonigarcia;

public class Stock {
    private String label;
    private int quantity;
    private double price;

    public Stock(String label, int quantity, double price) {
        this.label = label;
        this.quantity = quantity;
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}