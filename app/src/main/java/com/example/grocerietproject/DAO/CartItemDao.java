package com.example.grocerietproject.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.grocerietproject.entities.CartItem;

import java.util.List;

@Dao
public interface CartItemDao {

    @Query("SELECT * FROM CartItem WHERE UserId = :userId")
    List<CartItem> getCartItemsByUserId(int userId);

    @Query("SELECT * FROM CartItem WHERE ProductId =:productId and UserId =:userId")
    CartItem getCartItemsByProductId(int productId, int userId);

    @Query("DELETE FROM CartItem WHERE UserId = :userId")
    void clearCartByUserId(int userId);

    @Query("DELETE FROM CartItem WHERE cartItemId = :cartItemId")
    void deleteCartItemById(int cartItemId);

    @Insert
    void insertCartItem(CartItem cartItem);

    @Update
    void updateCartItem(CartItem cartItem);

    @Delete
    void deleteCartItem(CartItem cartItem);

    @Query("SELECT * FROM CartItem WHERE cartItemId = :cartItemId")
    CartItem getCartItemById(int cartItemId);
}
