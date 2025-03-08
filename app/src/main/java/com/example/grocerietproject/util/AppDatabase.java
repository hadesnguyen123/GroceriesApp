package com.example.grocerietproject.util;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.grocerietproject.DAO.CartItemDao;
import com.example.grocerietproject.DAO.CategoryDao;
import com.example.grocerietproject.DAO.OrderDao;
import com.example.grocerietproject.DAO.ProductDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.entities.CartItem;
import com.example.grocerietproject.entities.Category;
import com.example.grocerietproject.entities.Order;
import com.example.grocerietproject.entities.Product;
import com.example.grocerietproject.entities.Profile;
import com.example.grocerietproject.entities.User;

@Database(entities =
        {Product.class, User.class, Profile.class, Category.class, CartItem.class, Order.class},
        version = 3, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract UserDao userDao();
    public abstract CartItemDao cartItemDao();
    public abstract OrderDao orderDao();
    public abstract CategoryDao categoryDao();
}
