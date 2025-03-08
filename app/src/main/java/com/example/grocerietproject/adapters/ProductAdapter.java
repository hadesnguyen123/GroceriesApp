package com.example.grocerietproject.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.grocerietproject.DAO.CartItemDao;
import com.example.grocerietproject.DAO.ProductDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.entities.CartItem;
import com.example.grocerietproject.entities.Product;
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.models.ProductRecycle;
import com.example.grocerietproject.util.AppDatabase;
import com.example.grocerietproject.viewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    AppDatabase db;
    ProductDao productDao;
    CartItemDao cartItemDao;
    private List<ProductRecycle> mProducts;
    private Context mContext;
    private NavController navController;

    public ProductAdapter(List<ProductRecycle> mProducts, Context context, NavController navController) {
        this.mProducts = mProducts;
        this.mContext = context;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View productView = inflater.inflate(R.layout.product_rv_item, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(productView);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductRecycle product = mProducts.get(position);
        holder.productName.setText(product.getProductName());
        holder.productWeight.setText(product.getWeight());
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        holder.productAmount.setText(String.valueOf(product.getAmount()));
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("productId", product.getProductId());
            navController.navigate(R.id.action_homeFragment_to_productDetailFragment, bundle);
        });

        // Tải ảnh từ drawable theo tên danh mục
        String productName = product.getProductName();
        String imageName = "product_" + productName.toLowerCase().replace(" ", "_"); // Định dạng tên ảnh
        int imageResId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        if (imageResId != 0) {
            Picasso.get()
                    .load(imageResId)
                    .into(holder.imgProductItem);
        } else {
            holder.imgProductItem.setImageResource(R.drawable.product_apple); // Ảnh mặc định nếu không tìm thấy
        }

        holder.buttonProductItemAddToCart.setOnClickListener(v -> {
            initRoomDatabase();
            int productId = product.getProductId();
            //lay du lieu tren local
            SharedPreferences pref = mContext.getSharedPreferences("AccountPreference", Context.MODE_PRIVATE);
            int userId = pref.getInt("userId", 1);
            if (userId != 0) {
                CartItem existCartItem = cartItemDao.getCartItemsByProductId(productId, userId);
                if (existCartItem != null) {
                    existCartItem.quantity += 1;
                    cartItemDao.updateCartItem(existCartItem);
                } else {
                    CartItem newCartItem = new CartItem();
                    newCartItem.quantity = 1;
                    newCartItem.productId = productId;
                    newCartItem.userId = userId;
                    cartItemDao.insertCartItem(newCartItem);
                }
                Toast.makeText(v.getContext(), "Add to cart Success", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(v.getContext(), "You're login yet", Toast.LENGTH_LONG).show();
            }

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
        productDao = db.productDao();
        cartItemDao = db.cartItemDao();
    }
}
