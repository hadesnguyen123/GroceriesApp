package com.example.grocerietproject.fragments.categoryManagement;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.fragments.accountManagement.AdminAccountDetailFragment;
import com.example.grocerietproject.util.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminCateDetailFragment extends Fragment {
    AppDatabase db;
    CategoryDao categoryDao;

    Button buttonAdminCategoryDetailUpdate;
    ImageView imgAdminCategoryDetailBack;
    TextView edCategoryDetailName;

    public AdminCateDetailFragment() {
        super(R.layout.fragment_admin_category_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonAdminCategoryDetailUpdate = view.findViewById(R.id.buttonAdminCategoryDetailUpdate);
        imgAdminCategoryDetailBack = view.findViewById(R.id.imgAdminCategoryDetailBack);
        edCategoryDetailName = view.findViewById(R.id.edCategoryDetailName);
        imgAdminCategoryDetailBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminCateDetailFragment_to_adminCateListFragment);
        });
        initRoomDatabase();
        Category cateExist = null ;
        // Nhận userId từ arguments và tải thông tin người dùng
        if (getArguments() != null) {
            int cateId = getArguments().getInt("cateId", -1);
            if (cateId != -1) {
                cateExist = categoryDao.getCategoryById(cateId);
                if (cateExist != null) {
                    edCategoryDetailName.setText(cateExist.categoryName);
                }
            }
        }
        Category finalCateExist = cateExist;  // Để sử dụng trong onClickListener
        buttonAdminCategoryDetailUpdate.setOnClickListener(v -> {
            if (finalCateExist != null) {
                finalCateExist.categoryName = edCategoryDetailName.getText().toString();// Lấy dữ liệu mới từ các EditText
                new Thread(() -> {
                    categoryDao.update(finalCateExist);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this)
                                .navigate(R.id.action_adminCateDetailFragment_to_adminCateListFragment);
                    });
                }).start();
            }
        });
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
