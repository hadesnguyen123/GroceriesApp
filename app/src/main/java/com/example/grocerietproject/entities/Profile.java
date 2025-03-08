package com.example.grocerietproject.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = User.class,
                childColumns = "UserId",
                parentColumns = "userId",
                onDelete = ForeignKey.CASCADE
        )
}, indices = {@Index(value = "UserId")} // Thêm chỉ mục cho cột UserId
)
public class Profile {
    @PrimaryKey(autoGenerate = true)
    public int profileId;
    @ColumnInfo(name = "UserId")
    public int userId;
    @ColumnInfo(name = "FullName")
    public String fullName;
    @ColumnInfo(name = "DisplayName")
    public String displayName;
    @ColumnInfo(name = "Address")
    public String address;
    @ColumnInfo(name = "PhoneNumber")
    public String phoneNumber;
}
