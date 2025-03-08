package com.example.grocerietproject.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.grocerietproject.entities.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category")
    List<Category> getAllCategories();

    @Insert
    void insertCategory(Category category);

    @Query("SELECT * FROM Category WHERE categoryId = :categoryId")
    Category getCategoryById(int categoryId);

    @Query("SELECT CategoryName FROM Category WHERE categoryId =:categoryId")
    String getCategoryNameById(int categoryId);

    @Delete
    void deleteCategory(Category category);

    @Update
    void update(Category category);
}
