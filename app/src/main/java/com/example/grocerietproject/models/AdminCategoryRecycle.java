package com.example.grocerietproject.models;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class AdminCategoryRecycle {
    private int categoryId;
    private String categoryName;

    public AdminCategoryRecycle(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
