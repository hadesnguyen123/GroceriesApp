package com.example.grocerietproject.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int categoryId;
    @ColumnInfo(name = "CategoryName")
    public String categoryName;
    @ColumnInfo(name = "Image")
    public String image;
    @Override
    public String toString() {
        return categoryName;  //Trả về tên danh mục thay vì toàn bộ thông tin đối tượng
    }

    //Khi bạn sử dụng ArrayAdapter để hiển thị danh sách Category,
    // mặc định nó sẽ gọi phương thức toString() của đối tượng Category,
    // mà phương thức này có thể không trả về tên danh mục mà bạn mong muốn.
    // Để sửa lỗi này, bạn cần tùy chỉnh phương thức toString() của lớp Category hoặc cung cấp một
    // ArrayAdapter với định dạng rõ ràng cho Spinner.
}
