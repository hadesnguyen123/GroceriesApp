package com.example.grocerietproject.fragments.categoryManagement;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.grocerietproject.DAO.CategoryDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.adapters.AdminCategoryAdapter;
import com.example.grocerietproject.adapters.AdminCategoryAdapter;
import com.example.grocerietproject.entities.Category;
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.models.AdminAccountRecycle;
import com.example.grocerietproject.models.AdminCategoryRecycle;
import com.example.grocerietproject.models.AdminCategoryRecycle;
import com.example.grocerietproject.util.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AdminCateListFragment extends Fragment {
    ArrayList<AdminCategoryRecycle> adminCategoryRecycles;
    RecyclerView adminCategoryRecyclerView;
    AdminCategoryAdapter adminCategoryAdapter;
    AppDatabase db;
    UserDao userDao;
    CategoryDao categoryDao;
    NavController navController;
    ImageView imgButtonAddNew;
    ImageView imaButtonBack;

    public AdminCateListFragment() {
        super(R.layout.fragment_admin_category_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminCategoryRecyclerView = view.findViewById(R.id.viewRecycleAdminCategoryList);
        adminCategoryRecycles = new ArrayList<AdminCategoryRecycle>();
//        String[] myStringName = {"Dress", "Shirts"};
//        for (int i = 0; i < myStringName.length; i++) {
//            adminCategoryRecycles.add(new AdminCategoryRecycle(1, myStringName[i]));
//        }
        navController = NavHostFragment.findNavController(this);
        adminCategoryAdapter = new AdminCategoryAdapter(adminCategoryRecycles, getContext(), navController);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adminCategoryRecyclerView.setAdapter(adminCategoryAdapter);
        adminCategoryRecyclerView.setLayoutManager(linearLayoutManager);

        initRoomDatabase();
        loadCategorysFromDatabase();

        imgButtonAddNew = view.findViewById(R.id.imgAdminListCategoryAddNew);
        imaButtonBack = view.findViewById(R.id.imgAdminListCategoryBack);
        imgButtonAddNew.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminCateListFragment_to_adminCateAddFragment);
        });
        imaButtonBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminCateListFragment_to_profileFragment2);
        });
    }

    private void loadCategorysFromDatabase() {
        new Thread(() -> {
            List<Category> categories = categoryDao.getAllCategories();
            for (Category cate : categories) {
                adminCategoryRecycles.add(new AdminCategoryRecycle(cate.categoryId, cate.categoryName));
            }
            new Handler(Looper.getMainLooper()).post(() -> adminCategoryAdapter.notifyDataSetChanged());// Cập nhật giao diện trong main thread
        }).start();
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()   //Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number. Expected identity hash: db3c0ae21eaf52ebb7c432fc220cc6d0, found: 74d1741ddf4be43f64c6a8ae2afc19ed
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
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
