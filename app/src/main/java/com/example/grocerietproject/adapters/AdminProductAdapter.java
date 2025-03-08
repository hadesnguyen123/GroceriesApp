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
import com.example.grocerietproject.entities.Product;
import com.example.grocerietproject.models.AdminProductRecycle;
import com.example.grocerietproject.models.AdminProductRecycle;
import com.example.grocerietproject.util.AppDatabase;
import com.example.grocerietproject.viewHolder.AdminProductViewHolder;
import com.example.grocerietproject.viewHolder.AdminCategoryViewHolder;
import com.example.grocerietproject.viewHolder.AdminProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductViewHolder> {
    private List<AdminProductRecycle> mProducts;
    private Context mContext;
    private NavController navController;
    private AppDatabase db;
    private UserDao userDao;
    private ProductDao productDao;
    private CategoryDao categoryDao;

    public AdminProductAdapter(List<AdminProductRecycle> mProducts, Context mContext, NavController navController) {
        this.mProducts = mProducts;
        this.mContext = mContext;
        this.navController = navController;
    }

    @NonNull
    @Override
    public AdminProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View adminProductView = layoutInflater.inflate(R.layout.admin_product_rv_item, parent, false);
        AdminProductViewHolder viewHolder = new AdminProductViewHolder(adminProductView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductViewHolder holder, int position) {
        AdminProductRecycle adminProductRecycle = mProducts.get(position);
        holder.tvRecycleAdminProductName.setText(adminProductRecycle.getProductName());
        holder.tvRecycleAdminProductId.setText(String.valueOf(adminProductRecycle.getProductId()));
        holder.tvRecycleAdminProductCategory.setText(String.valueOf(adminProductRecycle.getCategoryName()));
        holder.tvRecycleAdminProductPrice.setText(String.valueOf(adminProductRecycle.getPrice()));
        holder.tvRecycleAdminProductAmount.setText(String.valueOf(adminProductRecycle.getAmount()));

        // Tải ảnh từ drawable theo tên danh mục
        String productName = adminProductRecycle.getProductName();
        String imageName = "product_" + productName.toLowerCase().replace(" ", "_"); // Định dạng tên ảnh
        int imageResId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        if (imageResId != 0) {
            Picasso.get()
                    .load(imageResId)
                    .into(holder.adminProductRecycleImage);
        } else {
            holder.adminProductRecycleImage.setImageResource(R.drawable.product_apple); // Ảnh mặc định nếu không tìm thấy
        }

        holder.buttonRecycleAdminProductDetail.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("productId", adminProductRecycle.getProductId());
            navController.navigate(R.id.action_adminProductListFragment_to_adminProductDetailFragment, bundle);
        });

        holder.buttonRecycleAdminProductDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure to delete this category ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        initRoomDatabase();
                        int productId = adminProductRecycle.getProductId();
                        new Thread(() -> {
                            Product productToDelete = productDao.getProductById(productId);
                            if (productToDelete != null) {
                                productDao.delete(productToDelete);
                            }
                            mProducts.remove(position);
                            ((Activity) mContext).runOnUiThread(() -> {
                                Toast.makeText(v.getContext(), "Xóa thành công", Toast.LENGTH_LONG).show();
                                navController.navigate(R.id.action_adminProductListFragment_self);
                            });
                        }).start();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
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
