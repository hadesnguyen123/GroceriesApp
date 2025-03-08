package com.example.grocerietproject.fragments.shopping;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.grocerietproject.DAO.ProductDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.fragments.loginRegister.AccountOptionFragment;
import com.example.grocerietproject.util.AppDatabase;

public class ProfileFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    ProductDao productDao;
    NavController navController;

    public ProfileFragment(){
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvProfileManageAccount = view.findViewById(R.id.tvProfileManageAccount);
        TextView tvProfileManageCategory= view.findViewById(R.id.tvProfileManageCategory);
        TextView tvProfileManageProduct= view.findViewById(R.id.tvProfileManageProduct);

        navController = NavHostFragment.findNavController(this);
        tvProfileManageAccount.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProfileFragment.this)
                    .navigate(R.id.action_profileFragment_to_adminAccountListFragment);
        });

        tvProfileManageCategory.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProfileFragment.this)
                    .navigate(R.id.action_profileFragment_to_adminCateListFragment);
        });

        tvProfileManageProduct.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProfileFragment.this)
                    .navigate(R.id.action_profileFragment_to_adminProductListFragment);
        });
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
        productDao = db.productDao();
    }
}
