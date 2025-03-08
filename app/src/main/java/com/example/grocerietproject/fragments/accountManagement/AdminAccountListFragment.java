package com.example.grocerietproject.fragments.accountManagement;

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

import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.adapters.AdminAccountAdapter;
import com.example.grocerietproject.adapters.CartItemAdapter;
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.fragments.shopping.ProfileFragment;
import com.example.grocerietproject.models.AdminAccountRecycle;
import com.example.grocerietproject.models.CartItemRecycle;
import com.example.grocerietproject.util.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AdminAccountListFragment extends Fragment {
    ArrayList<AdminAccountRecycle> adminAccountRecycles;
    RecyclerView adminAccountRecyclerView;
    AdminAccountAdapter adminAccountAdapter;
    AppDatabase db;
    UserDao userDao;
    NavController navController;
    ImageView imgButtonAddNew;
    ImageView imaButtonBack;

    public AdminAccountListFragment() {
        super(R.layout.fragment_admin_account_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminAccountRecyclerView = view.findViewById(R.id.viewRecycleAdminAccountList);
        adminAccountRecycles = new ArrayList<AdminAccountRecycle>();
        navController = NavHostFragment.findNavController(this);
        adminAccountAdapter = new AdminAccountAdapter(adminAccountRecycles, getContext(), navController);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adminAccountRecyclerView.setAdapter(adminAccountAdapter);
        adminAccountRecyclerView.setLayoutManager(linearLayoutManager);

        initRoomDatabase();
        loadAccountsFromDatabase();

        imgButtonAddNew = view.findViewById(R.id.imgAdminListAccountAddNew);
        imaButtonBack = view.findViewById(R.id.imgAdminListAccountBack);
        imgButtonAddNew.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminAccountListFragment_to_adminAccountAddFragment);
        });
        imaButtonBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_adminAccountListFragment_to_profileFragment);
        });
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()   //Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number. Expected identity hash: db3c0ae21eaf52ebb7c432fc220cc6d0, found: 74d1741ddf4be43f64c6a8ae2afc19ed
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
    }

    private void loadAccountsFromDatabase() {
        new Thread(() -> {
            List<User> users = userDao.getAllUsers();
            for (User user : users) {
                adminAccountRecycles.add(new AdminAccountRecycle(
                        user.userId,
                        user.email,
                        user.role,
                        user.fullName));
            }
            // Cập nhật giao diện trong main thread
            new Handler(Looper.getMainLooper()).post(() -> adminAccountAdapter.notifyDataSetChanged());
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
