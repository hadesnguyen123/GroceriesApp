package com.example.grocerietproject.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(
        entity = Category.class,
        parentColumns = "categoryId",
        childColumns = "CategoryId",
        onDelete = ForeignKey.CASCADE))
// ?? not understand yet
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int productId;
    @ColumnInfo(name = "CategoryId")
    public int categoryId;
    @ColumnInfo(name = "Name")
    public String name;
    @ColumnInfo(name = "Price")
    public Double price;
    @ColumnInfo(name = "Description")
    public String description;
    @ColumnInfo(name = "Weight")
    public String weight;
    @ColumnInfo(name = "Image")
    public String image;
    @ColumnInfo(name = "Label") // Maybe: Best Seller
    public String label;
    @ColumnInfo(name = "Amount") // Maybe: Best Seller
    public int amount;
}
