package com.example.grocerietproject.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "userId",
        childColumns = "UserId",
        onDelete = ForeignKey.CASCADE))
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int orderId;
    @ColumnInfo(name = "UserId")
    public int userId;
    @ColumnInfo(name = "TotalAmount")
    public Double totalAmount;
    @ColumnInfo(name = "OrderDate")
    public String orderDate;
}
