package com.example.grocerietproject.adapters;

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
import com.example.grocerietproject.entities.Category;
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.models.AdminAccountRecycle;
import com.example.grocerietproject.models.AdminCategoryRecycle;
import com.example.grocerietproject.util.AppDatabase;
import com.example.grocerietproject.viewHolder.AdminAccountViewHolder;
import com.example.grocerietproject.viewHolder.AdminCategoryViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryViewHolder> {
    private List<AdminCategoryRecycle> mCategorys;
    private Context mContext;
    private NavController navController;
    private AppDatabase db;
    private UserDao userDao;
    private ProductDao productDao;
    private CategoryDao categoryDao;

    public AdminCategoryAdapter(List<AdminCategoryRecycle> mCategorys, Context mContext, NavController navController) {
        this.mCategorys = mCategorys;
        this.mContext = mContext;
        this.navController = navController;
    }

    @NonNull
    @Override
    public AdminCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View adminCategoryView = layoutInflater.inflate(R.layout.admin_category_rv_item, parent, false);
        AdminCategoryViewHolder viewHolder = new AdminCategoryViewHolder(adminCategoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCategoryViewHolder holder, int position) {
        AdminCategoryRecycle adminCategoryRecycle = mCategorys.get(position);

        holder.tvRecycleCategoryId.setText(String.valueOf(adminCategoryRecycle.getCategoryId()));
        holder.tvRecycleCategoryName.setText(adminCategoryRecycle.getCategoryName());
        holder.tvRecycleCategoryDescription.setText("None");
        holder.buttonRecycleAdminCategoryDetail.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("cateId", adminCategoryRecycle.getCategoryId());
            navController.navigate(R.id.action_adminCateListFragment_to_adminCateDetailFragment, bundle);
        });
        // Tải ảnh từ drawable theo tên danh mục
        String categoryName = adminCategoryRecycle.getCategoryName();
        String imageName = "category_" + categoryName.toLowerCase().replace(" ", "_"); // Định dạng tên ảnh
        int imageResId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        if (imageResId != 0) {
            Picasso.get()
                    .load(imageResId)
                    .into(holder.imgAdminCategoryRecycleImage);
        } else {
            holder.imgAdminCategoryRecycleImage.setImageResource(R.drawable.category_vegetable); // Ảnh mặc định nếu không tìm thấy
        }

        holder.buttonRecycleAdminCategoryDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure to delete this category ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        initRoomDatabase();
                        int categoryId = adminCategoryRecycle.getCategoryId();
                        new Thread(() -> {
                            Category categoryToDelete = categoryDao.getCategoryById(categoryId);
                            if (categoryToDelete != null) {
                                categoryDao.deleteCategory(categoryToDelete);
                            }
                            mCategorys.remove(position);
                            ((Activity) mContext).runOnUiThread(() -> {
                                Toast.makeText(v.getContext(), "Xóa thành công", Toast.LENGTH_LONG).show();
                                navController.navigate(R.id.action_adminCateListFragment_self);
                            });
                        }).start();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return mCategorys.size();
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
