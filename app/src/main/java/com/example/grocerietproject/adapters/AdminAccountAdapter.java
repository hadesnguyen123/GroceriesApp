package com.example.grocerietproject.adapters;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.grocerietproject.DAO.CategoryDao;
import com.example.grocerietproject.DAO.ProductDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.models.AdminAccountRecycle;
import com.example.grocerietproject.models.CartItemRecycle;
import com.example.grocerietproject.models.ProductRecycle;
import com.example.grocerietproject.util.AppDatabase;
import com.example.grocerietproject.viewHolder.AdminAccountViewHolder;
import com.example.grocerietproject.viewHolder.ProductViewHolder;

import java.util.List;

public class AdminAccountAdapter extends RecyclerView.Adapter<AdminAccountViewHolder> {
    private List<AdminAccountRecycle> mAccounts;
    private Context mContext;
    private NavController navController;
    private AppDatabase db;
    private UserDao userDao;
    private ProductDao productDao;
    private CategoryDao categoryDao;

    public AdminAccountAdapter(List<AdminAccountRecycle> mAccounts, Context mContext, NavController navController) {
        this.mAccounts = mAccounts;
        this.mContext = mContext;
        this.navController = navController;
    }

    @NonNull
    @Override
    public AdminAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View adminAcountView = layoutInflater.inflate(R.layout.account_rv_item, parent, false);
        AdminAccountViewHolder viewHolder = new AdminAccountViewHolder(adminAcountView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAccountViewHolder holder, int position) {
        AdminAccountRecycle adminAccountRecycle = mAccounts.get(position);
        holder.tvRecycleAccountEmail.setText(adminAccountRecycle.getEmail());
        holder.tvRecycleAccountId.setText(String.valueOf(adminAccountRecycle.getUserId()));
        holder.tvRecycleAccountFullName.setText(adminAccountRecycle.getFullName());
        if (adminAccountRecycle.getRole() == 1) {
            holder.tvRecycleAccountRole.setText("ADMIN");
        } else {
            holder.tvRecycleAccountRole.setText("USER");
        }
        holder.buttonRecycleAdminAccountDetail.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("userId", adminAccountRecycle.getUserId());
            navController.navigate(R.id.action_adminAccountListFragment_to_adminAccountDetailFragment, bundle);
        });
        holder.buttonRecycleAdminAccountDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this account?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        initRoomDatabase();
                        int accountRecycleId = adminAccountRecycle.getUserId();
                        new Thread(() -> {
                            User accountToDelete = userDao.getUserById(accountRecycleId);
                            if (accountToDelete != null) {
                                userDao.deleteUser(accountToDelete);
                            }
                            mAccounts.remove(position);
                            ((Activity) mContext).runOnUiThread(() -> {
                                Toast.makeText(v.getContext(), "Xóa thành công", Toast.LENGTH_LONG).show();
                                navController.navigate(R.id.action_adminAccountListFragment_self);
                            });
                        }).start();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return mAccounts.size();
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()   //Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number. Expected identity hash: db3c0ae21eaf52ebb7c432fc220cc6d0, found: 74d1741ddf4be43f64c6a8ae2afc19ed
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
        productDao = db.productDao();
        categoryDao = db.categoryDao();
    }
}
