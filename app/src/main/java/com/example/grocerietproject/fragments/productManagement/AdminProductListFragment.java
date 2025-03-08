package com.example.grocerietproject.fragments.productManagement;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.grocerietproject.DAO.CategoryDao;
import com.example.grocerietproject.DAO.ProductDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.adapters.AdminProductAdapter;
import com.example.grocerietproject.entities.Product;
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.models.AdminAccountRecycle;
import com.example.grocerietproject.models.AdminProductRecycle;
import com.example.grocerietproject.util.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AdminProductListFragment extends Fragment {
    ArrayList<AdminProductRecycle> adminProductRecycles;
    RecyclerView adminProductRecyclerView;
    AdminProductAdapter adminProductAdapter;
    AppDatabase db;
    UserDao userDao;
    ProductDao productDao;
    CategoryDao categoryDao;
    NavController navController;
    ImageView imgButtonAddNew;
    ImageView imaButtonBack;

    public AdminProductListFragment() {
        super(R.layout.fragment_admin_product_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminProductRecyclerView = view.findViewById(R.id.viewRecycleAdminProductList);
        adminProductRecycles = new ArrayList<AdminProductRecycle>();
        String[] myStringName = {"Dress", "Shirts"};
//        for (int i = 0; i < myStringName.length; i++) {
//            adminProductRecycles.add(new AdminProductRecycle(1, "nguyenhoang@gmail.com", 1, myStringName[i]));
//        }
        navController = NavHostFragment.findNavController(this);
        adminProductAdapter = new AdminProductAdapter(adminProductRecycles, getContext(), navController);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adminProductRecyclerView.setAdapter(adminProductAdapter);
        adminProductRecyclerView.setLayoutManager(linearLayoutManager);

        initRoomDatabase();
        loadProductsFromDatabase();

        imgButtonAddNew = view.findViewById(R.id.imgAdminListProductAddNew);
        imaButtonBack = view.findViewById(R.id.imgAdminListProductBack);
        imgButtonAddNew.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminProductListFragment_to_adminProductAddFragment);
        });
        imaButtonBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminProductListFragment_to_profileFragment);
        });

    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()   //Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number. Expected identity hash: db3c0ae21eaf52ebb7c432fc220cc6d0, found: 74d1741ddf4be43f64c6a8ae2afc19ed
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
        productDao = db.productDao();
        categoryDao = db.categoryDao();
    }

    private void loadProductsFromDatabase() {
        new Thread(() -> {
            List<Product> products = productDao.getAll();
            for (Product product : products) {
                String categoryName = categoryDao.getCategoryNameById(product.categoryId);
                adminProductRecycles.add(new AdminProductRecycle(
                        product.productId,
                        categoryName,
                        product.name,
                        product.price,
                        product.description,
                        product.weight,
                        product.label,
                        product.amount
                ));
            }
            // Cập nhật giao diện trong main thread
            new Handler(Looper.getMainLooper()).post(() -> adminProductAdapter.notifyDataSetChanged());
        }).start();
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
