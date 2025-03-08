package com.example.grocerietproject.models;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class AdminProductRecycle {
    private int productId;
    private String categoryName;
    private String productName;
    private double price;
    private String description;
    private String weight;
    private String label;
    private int amount = 10;

    public AdminProductRecycle(int productId, String categoryName, String productName, double price, String description, String weight, String label, int amount) {
        this.productId = productId;
        this.categoryName = categoryName;
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.weight = weight;
        this.label = label;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


}


