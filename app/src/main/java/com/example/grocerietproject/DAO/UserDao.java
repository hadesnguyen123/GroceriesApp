package com.example.grocerietproject.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.grocerietproject.entities.Profile;
import com.example.grocerietproject.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM User WHERE userId=:userId")
    User getUserById(int userId);

    @Query("SELECT * FROM User WHERE email =:email")
    User getEmailById(String email);

    @Query("SELECT * FROM User WHERE email = :email AND passWord =:password")
    User login(String email,String password);

    @Insert
    void register(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM Profile WHERE userId = :userId")
    Profile getProfileByUserId(int userId); // Lấy thông tin profile theo userId

    @Query("UPDATE User SET passWord = :newPassword WHERE userId = :userId")
    void updatePassword(int userId, String newPassword); // Cập nhật mật khẩu

    @Update
    void updateProfile(Profile profile); // Cập nhật thông tin profile

    @Update
    void update(User user);
}
