package com.example.grocerietproject.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.grocerietproject.entities.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM `Order` WHERE userId = :userId")
    List<Order> getOrdersByUserId(int userId);

    @Insert
    void insertOrder(Order order);

    @Query("SELECT * FROM `Order` WHERE orderId = :orderId")
    Order getOrderById(int orderId);

    @Delete
    void deleteOrder(Order order);
}
