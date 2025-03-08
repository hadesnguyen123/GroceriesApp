package com.example.grocerietproject.DAO;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.grocerietproject.entities.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM Product")
    List<Product> getAll();

    @Query("SELECT * FROM PRODUCT WHERE productId =:productId")
    Product getProductById(int productId);

    @Query("SELECT * FROM PRODUCT WHERE categoryId =:categoryId")
    List<Product> getProductByCategoryId(int categoryId);

    @Query("SELECT * FROM Product WHERE label = 'Best Selling'")
    List<Product> getBestSellers();

    @Query("SELECT * FROM Product WHERE label = 'Exclusive Offer'")
    List<Product> getExclusiveOffers();

    @Query("SELECT * FROM Product WHERE label = 'New Arrival'")
    List<Product> getNewArrival();

    @Query("SELECT * FROM Product WHERE name =:first ")
    Product findName(String first);

    @Query("SELECT Amount FROM Product WHERE productId =:productId ")
    int getProductAmount(int productId);

    @Insert
    void insertAll(Product... products);

    @Delete
    void delete(Product product);

    @Update
    void update(Product product);

}
