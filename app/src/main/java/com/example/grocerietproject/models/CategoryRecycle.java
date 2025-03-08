package com.example.grocerietproject.models;

public class CategoryRecycle {
    private int categoryId;
    private String name;
    private String color;

    public CategoryRecycle(int categoryId, String color, String name) {
        this.categoryId = categoryId;
        this.color = color;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
