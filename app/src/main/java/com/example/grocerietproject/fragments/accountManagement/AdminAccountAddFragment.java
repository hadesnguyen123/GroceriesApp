package com.example.grocerietproject.fragments.accountManagement;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.models.AdminAccountRecycle;
import com.example.grocerietproject.util.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

public class AdminAccountAddFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    Button buttonAdminAccountAddAdd;
    ImageView imgAdminAccountAddBack;
    EditText edAccountAddEmail;
    EditText edAccountAddRole;
    EditText edAccountAddFullName;
    EditText edAccountAddPassword;

    public AdminAccountAddFragment() {
        super(R.layout.fragment_admin_account_add);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonAdminAccountAddAdd = view.findViewById(R.id.buttonAdminAccountAddAdd);
        imgAdminAccountAddBack = view.findViewById(R.id.imgAdminAccountAddBack);
        edAccountAddEmail = view.findViewById(R.id.edAccountAddEmail);
        edAccountAddRole = view.findViewById(R.id.edAccountAddRole);
        edAccountAddFullName = view.findViewById(R.id.edAccountAddFullName);
        edAccountAddPassword = view.findViewById(R.id.edAccountAddPassword);

        //Quay trở lại danh sách
        imgAdminAccountAddBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminAccountAddFragment_to_adminAccountListFragment);
        });

        initRoomDatabase();
        //Thêm sản phẩm
        buttonAdminAccountAddAdd.setOnClickListener(v -> {
            // Capture the input from the EditText fields
            String email = edAccountAddEmail.getText().toString();
            String roleText = edAccountAddRole.getText().toString();
            String fullName = edAccountAddFullName.getText().toString();
            String password = edAccountAddPassword.getText().toString();

            // Validate the input
            if (email.isEmpty() || roleText.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            int role;
            try {
                role = Integer.parseInt(roleText);  // Convert role input to integer
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Role must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            //tạo mới user
            User newUser = new User();
            newUser.email = email;
            newUser.fullName = fullName;
            newUser.role = role;
            newUser.passWord = password;

            new Thread(() -> {
                userDao.insertUser(newUser);
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getContext(), "User added successfully", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(AdminAccountAddFragment.this)
                            .navigate(R.id.action_adminAccountAddFragment_to_adminAccountListFragment);
                });
            }).start();
        });
    }


    private void initRoomDatabase() {
        db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()   //Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number. Expected identity hash: db3c0ae21eaf52ebb7c432fc220cc6d0, found: 74d1741ddf4be43f64c6a8ae2afc19ed
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
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
