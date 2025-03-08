package com.example.grocerietproject.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;
    @ColumnInfo(name = "PassWord")
    public String passWord;
    @ColumnInfo(name ="Email")
    public String email;
    @ColumnInfo(name = "Role")
    public int role;
    @ColumnInfo(name = "FullName")
    public String fullName;

}
