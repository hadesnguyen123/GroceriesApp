package com.example.grocerietproject.fragments.categoryManagement;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.grocerietproject.DAO.CategoryDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.entities.Category;
import com.example.grocerietproject.fragments.accountManagement.AdminAccountAddFragment;
import com.example.grocerietproject.util.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminCateAddFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    CategoryDao categoryDao;
    Button buttonAdminCategoryAddAdd;
    ImageView imgAdminCategoryAddBack;
    EditText edCategoryAddName;

    public AdminCateAddFragment() {
        super(R.layout.fragment_admin_category_add);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonAdminCategoryAddAdd = view.findViewById(R.id.buttonAdminCategoryAddAdd);
        imgAdminCategoryAddBack = view.findViewById(R.id.imgAdminCategoryAddBack);
        edCategoryAddName = view.findViewById(R.id.edCategoryAddName);

        initRoomDatabase();

        buttonAdminCategoryAddAdd.setOnClickListener(v -> {
            String categoryName = edCategoryAddName.getText().toString();
            if (categoryName.isEmpty()) { //validate
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            Category newCategory = new Category();
            newCategory.categoryName = categoryName;
            new Thread(() -> {
                categoryDao.insertCategory(newCategory);
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getContext(), "Category added successfully", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_adminCateAddFragment_to_adminCateListFragment);
                });
            }).start();
        });

        imgAdminCategoryAddBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminCateAddFragment_to_adminCateListFragment);
        });
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()   //Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number. Expected identity hash: db3c0ae21eaf52ebb7c432fc220cc6d0, found: 74d1741ddf4be43f64c6a8ae2afc19ed
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
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
