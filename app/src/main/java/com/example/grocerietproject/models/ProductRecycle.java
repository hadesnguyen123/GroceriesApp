package com.example.grocerietproject.models;

public class ProductRecycle {
    private int productId;
    private String categoryName;
    private String productName;
    private double price;
    private String description;
    private String weight;
    private String label;
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ProductRecycle(int productId, String categoryName, String productName, String label, String description, double price, String weight, int amount) {
        this.productId = productId;
        this.categoryName = categoryName;
        this.productName = productName;
        this.label = label;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.amount = amount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
