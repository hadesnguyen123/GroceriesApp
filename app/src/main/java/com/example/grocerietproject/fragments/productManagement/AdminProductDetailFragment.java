package com.example.grocerietproject.fragments.productManagement;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
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
import com.example.grocerietproject.util.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class AdminProductDetailFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    ProductDao productDao;
    CategoryDao categoryDao;
    Button buttonAdminProductDetailUpdate;
    ImageView imgAdminProductDetailBack;
    EditText edProductDetailName, edProductDetailWeight, edProductDetailDescription, edProductDetailPrice, edProductDetailAmount;
    Spinner spinnerCategory;
    Spinner spinnerProductLabel;

    public AdminProductDetailFragment() {
        super(R.layout.fragment_admin_product_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Khởi tạo các phần tử UI
        buttonAdminProductDetailUpdate = view.findViewById(R.id.buttonAdminProductDetailUpdate);
        imgAdminProductDetailBack = view.findViewById(R.id.imgAdminProductDetailBack);
        edProductDetailName = view.findViewById(R.id.edProductDetailName);
        edProductDetailWeight = view.findViewById(R.id.edProductDetailWeight);
        edProductDetailDescription = view.findViewById(R.id.edProductDetailDescription);
        edProductDetailPrice = view.findViewById(R.id.edProductDetailPrice);
        edProductDetailAmount = view.findViewById(R.id.edProductDetailAmount);
        spinnerProductLabel = view.findViewById(R.id.spinnerProductDetailLabel);
        spinnerCategory = view.findViewById(R.id.spinnerProductDetailCategory);

        initRoomDatabase();

        int productId = getArguments().getInt("productId");
        Product productExist = productDao.getProductById(productId);
        if (productExist != null) {
            edProductDetailName.setText(productExist.name);
            edProductDetailWeight.setText(productExist.weight);
            edProductDetailDescription.setText(productExist.description);
            edProductDetailPrice.setText(String.valueOf(productExist.price));
            edProductDetailAmount.setText(String.valueOf(productExist.amount));

            //label to spinner
            ArrayAdapter<String> labelAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                    new String[]{"Best Selling", "Expensive", "New Arrival", "Discounted"});
            labelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProductLabel.setAdapter(labelAdapter);
            int labelPosition = labelAdapter.getPosition(productExist.label);
            spinnerProductLabel.setSelection(labelPosition);

            // Gắn danh mục vào spinner
            List<Category> categoryList = categoryDao.getAllCategories();
            ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                    categoryList);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(categoryAdapter);
            int categoryPosition = -1;
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).categoryId == productExist.categoryId) {
                    categoryPosition = i;
                    break;
                }
            }
            if (categoryPosition != -1) {
                spinnerCategory.setSelection(categoryPosition); // Chọn danh mục đúng
            }
            buttonAdminProductDetailUpdate.setOnClickListener(v -> {
                productExist.name = edProductDetailName.getText().toString();
                productExist.weight = edProductDetailWeight.getText().toString();
                productExist.description = edProductDetailWeight.getText().toString();
                productExist.price = Double.parseDouble(edProductDetailPrice.getText().toString());
                productExist.label = spinnerProductLabel.getSelectedItem().toString();
                productExist.amount = Integer.parseInt(edProductDetailAmount.getText().toString());
                Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
                productExist.categoryId = selectedCategory.categoryId;
                new Thread(() -> {
                    Log.d("UpdateDetails", "Product updated successfully");
                    productDao.update(productExist); // Thực hiện cập nhật
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this)
                                .navigate(R.id.action_adminProductDetailFragment_to_adminProductListFragment);
                    });
                }).start();
            });
        } else {
            Toast.makeText(getContext(), "Product not found", Toast.LENGTH_SHORT).show();
        }

        imgAdminProductDetailBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminProductDetailFragment_to_adminProductListFragment);
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
