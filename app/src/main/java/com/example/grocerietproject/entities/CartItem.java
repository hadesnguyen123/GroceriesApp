package com.example.grocerietproject.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = User.class,
                childColumns = "UserId",
                parentColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Product.class,
                parentColumns = "productId",
                childColumns = "ProductId",
                onDelete = ForeignKey.CASCADE
        )
})
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    public int cartItemId;
    @ColumnInfo(name = "UserId")
    public int userId;
    @ColumnInfo(name = "ProductId")
    public int productId;
    @ColumnInfo(name = "Quantity")
    public int quantity;
}
