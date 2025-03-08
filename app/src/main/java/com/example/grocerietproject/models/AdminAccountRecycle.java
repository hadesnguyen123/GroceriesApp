package com.example.grocerietproject.models;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class AdminAccountRecycle {
    private int userId;
    private String email;
    private int role;
    private String fullName;


    public AdminAccountRecycle(int userId, String email, int role, String fullName) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.fullName = fullName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
