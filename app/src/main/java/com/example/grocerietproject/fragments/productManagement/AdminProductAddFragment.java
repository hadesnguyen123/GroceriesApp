package com.example.grocerietproject.fragments.productManagement;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.grocerietproject.DAO.CategoryDao;
import com.example.grocerietproject.DAO.ProductDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.entities.Category;
import com.example.grocerietproject.entities.Product;
import com.example.grocerietproject.fragments.accountManagement.AdminAccountAddFragment;
import com.example.grocerietproject.models.AdminCategoryRecycle;
import com.example.grocerietproject.util.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AdminProductAddFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    ProductDao productDao;
    CategoryDao categoryDao;
    Button buttonAdminProductAddAdd;
    ImageView imgAdminProductAddBack;
    EditText edProductAddName, edProductAddWeigh, edProductAddDescription, edProductAddPrice;
    Spinner spinnerCategory;
    Spinner spinnerProductLabel;

    public AdminProductAddFragment() {
        super(R.layout.fragment_admin_product_add);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Khởi tạo các phần tử UI
        buttonAdminProductAddAdd = view.findViewById(R.id.buttonAdminProductAddAdd);
        imgAdminProductAddBack = view.findViewById(R.id.imgAdminProductAddBack);
        edProductAddName = view.findViewById(R.id.edProductAddName);
        edProductAddWeigh = view.findViewById(R.id.edProductAddWeigh);
        edProductAddDescription = view.findViewById(R.id.edProductAddDescription);
        edProductAddPrice = view.findViewById(R.id.edProductAddPrice);
        spinnerProductLabel = view.findViewById(R.id.spinnerProductAddLabel);
        spinnerCategory = view.findViewById(R.id.spinnerProductAddCategory);

        initRoomDatabase();

        // Tạo mảng dữ liệu cho Spinner của Product Label
        String[] productLabels = new String[]{"Best Selling", "Expensive", "New Arrival", "Discounted"};
        ArrayAdapter<String> labelAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, productLabels);
        labelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductLabel.setAdapter(labelAdapter);

        // Lấy danh sách tên danh mục từ cơ sở dữ liệu
        List<Category> categoryList = categoryDao.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categoryList) {
            categoryNames.add(category.categoryName);  // Chỉ lấy tên danh mục
        }
        // Tạo adapter cho Spinner của Category
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);  // Gắn adapter vào Spinner

        //Sự kiên
        buttonAdminProductAddAdd.setOnClickListener(v -> {
            String productName = edProductAddName.getText().toString();
            String productWeight = edProductAddWeigh.getText().toString();
            String productDescription = edProductAddDescription.getText().toString();
            String productPrice = edProductAddPrice.getText().toString();

            if (productName.isEmpty() || productWeight.isEmpty() || productDescription.isEmpty() || productPrice.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }
            String productLabel = (String) spinnerProductLabel.getSelectedItem(); //lấy label
            int selectedCategoryPosition = spinnerCategory.getSelectedItemPosition();
            int categoryId = categoryList.get(selectedCategoryPosition).categoryId; // Lấy categoryId từ danh sách categoryList

            //add product
            Product productNew = new Product();
            productNew.name = productName;
            productNew.weight = productWeight;
            productNew.description = productDescription;
            productNew.price = Double.parseDouble(productPrice);
            productNew.label = productLabel;
            productNew.categoryId = categoryId;
            new Thread(() -> {
                productDao.insertAll(productNew);
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_adminProductAddFragment_to_adminProductListFragment);
                });
            }).start();
        });
        imgAdminProductAddBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminProductAddFragment_to_adminProductListFragment);
        });

    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()   //Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number. Expected identity hash: db3c0ae21eaf52ebb7c432fc220cc6d0, found: 74d1741ddf4be43f64c6a8ae2afc19ed
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
        productDao = db.productDao();
        categoryDao = db.categoryDao();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Hiển thị lại thanh điều hướng khi rời khỏi trang chi tiết
        View bottomNavigationView = getActivity().findViewById(R.id.bottomNavigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Ẩn thanh điều hướng khi vào trang chi tiết
        if (getActivity() != null) {
            BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.bottomNavigation);
            if (bottomNavigation != null) {
                bottomNavigation.setVisibility(View.GONE);
            }
        }
    }
}
