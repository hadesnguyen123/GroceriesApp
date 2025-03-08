package com.example.grocerietproject.fragments.accountManagement;

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

import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.util.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class AdminAccountDetailFragment extends Fragment {
    Button buttonAdminAccountDetailUpdate;
    ImageView imgAdminAccountDetailBack;
    AppDatabase db;
    UserDao userDao;
    TextView edAccountDetailEmail,edAccountDetailRole,edAccountDetailPasswrod,edAccountDetailFullName;


    public AdminAccountDetailFragment() {
        super(R.layout.fragment_admin_account_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonAdminAccountDetailUpdate = view.findViewById(R.id.buttonAdminAccountDetailUpdate);
        imgAdminAccountDetailBack = view.findViewById(R.id.imgAdminAccountDetailBack);
        imgAdminAccountDetailBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminAccountDetailFragment_to_adminAccountListFragment);
        });
        edAccountDetailEmail = view.findViewById(R.id.edAccountDetailEmail);
        edAccountDetailRole = view.findViewById(R.id.edAccountDetailRole);
        edAccountDetailPasswrod = view.findViewById(R.id.edAccountDetailPasswrod);
        edAccountDetailFullName = view.findViewById(R.id.edAccountDetailFullName);
        // Khởi tạo UserDao
        initRoomDatabase();
        // Biến để lưu trữ người dùng hiện tại
        User userExist = null;
        // Nhận userId từ arguments và tải thông tin người dùng
        if (getArguments() != null) {
            int userId = getArguments().getInt("userId", -1);
            if (userId != -1) {
                userExist = userDao.getUserById(userId);
                if (userExist != null) {
                    // Hiển thị thông tin người dùng lên các EditText
                    edAccountDetailEmail.setText(userExist.email);
                    edAccountDetailRole.setText(String.valueOf(userExist.role));
                    edAccountDetailFullName.setText(userExist.fullName);
                    edAccountDetailPasswrod.setText(userExist.passWord);
                }
            }
        }
        User finalUserExist = userExist;  // Để sử dụng trong onClickListener
        // Sự kiện cập nhật thông tin người dùng
        buttonAdminAccountDetailUpdate.setOnClickListener(v -> {
            if (finalUserExist != null) {
                // Lấy dữ liệu mới từ các EditText
                finalUserExist.email = edAccountDetailEmail.getText().toString();
                try {
                    finalUserExist.role = (Integer.parseInt(edAccountDetailRole.getText().toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Role phải là số nguyên", Toast.LENGTH_SHORT).show();
                    return;
                }
                finalUserExist.fullName = (edAccountDetailFullName.getText().toString());
                finalUserExist.passWord = (edAccountDetailPasswrod.getText().toString());

                // Cập nhật vào cơ sở dữ liệu
                new Thread(() -> {
                    userDao.update(finalUserExist);
                    // Trở về danh sách tài khoản sau khi cập nhật
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(AdminAccountDetailFragment.this)
                                .navigate(R.id.action_adminAccountDetailFragment_to_adminAccountListFragment);
                    });
                }).start();
            }
        });
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "database-name")
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
