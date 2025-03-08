package com.example.grocerietproject.models;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class CartItemRecycle {
    private int cartItemId;
    private int productId;
    private int quantity;
    private String productName;
    private String productWeigh;
    private double prices;

    public CartItemRecycle(int cartItemId, int productId, int quantity, String productName, String productWeigh, double prices) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productWeigh = productWeigh;
        this.prices = prices;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductWeigh() {
        return productWeigh;
    }

    public void setProductWeigh(String productWeigh) {
        this.productWeigh = productWeigh;
    }

    public double getPrices() {
        return prices;
    }

    public void setPrices(double prices) {
        this.prices = prices;
    }
}
