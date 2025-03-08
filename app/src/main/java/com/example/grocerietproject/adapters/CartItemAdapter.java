package com.example.grocerietproject.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import com.example.grocerietproject.entities.Category;
import com.example.grocerietproject.entities.Product;
import com.example.grocerietproject.models.CartItemRecycle;
import com.example.grocerietproject.models.CategoryRecycle;
import com.example.grocerietproject.models.ProductRecycle;
import com.example.grocerietproject.util.AppDatabase;
import com.example.grocerietproject.viewHolder.CartItemViewHolder;
import com.example.grocerietproject.viewHolder.CategoryViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemViewHolder> {
    AppDatabase db;
    ProductDao productDao;
    CartItemDao cartItemDao;
    private List<CartItemRecycle> mCartItems;
    private Context mContext;
    private NavController navController;

    public CartItemAdapter(List<CartItemRecycle> mCartItems, Context mContext, NavController navController) {
        this.mCartItems = mCartItems;
        this.mContext = mContext;
        this.navController = navController;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cartItemView = inflater.inflate(R.layout.cart_rv_item, parent, false);
        CartItemViewHolder cartItemViewHolder = new CartItemViewHolder(cartItemView);
        return cartItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        //get cart item đang được giữ bởi viewholder
        CartItemRecycle cartItemRecycle = mCartItems.get(position);
        initRoomDatabase();
        CartItem cartItem = cartItemDao.getCartItemById(cartItemRecycle.getCartItemId());
        holder.tvCartItemProductName.setText(cartItemRecycle.getProductName());
        holder.tvCartItemProductWeigh.setText(cartItemRecycle.getProductWeigh());
        holder.tvCartItemPrice.setText(String.valueOf(cartItemRecycle.getPrices()));
        holder.tvCartItemQuantity.setText(String.valueOf(cartItemRecycle.getQuantity()));
        // Tải ảnh từ drawable theo tên danh mục
        String productName = cartItemRecycle.getProductName();
        String imageName = "product_" + productName.toLowerCase().replace(" ", "_");
        int imageResId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        if (imageResId != 0) {
            Picasso.get()
                    .load(imageResId)
                    .into(holder.imgCartItemImage);
        } else {
            holder.imgCartItemImage.setImageResource(R.drawable.product_apple);
        }

        holder.imgButtonDeleteCartItem.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new Thread(() -> {
                            cartItemDao.deleteCartItemById(cartItemRecycle.getCartItemId());
                            ((Activity) mContext).runOnUiThread(() -> {
                                Toast.makeText(v.getContext(), "Delete Success", Toast.LENGTH_LONG).show();
                                navController.navigate(R.id.action_cartFragment_self);
                            });
                        }).start();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
        holder.buttonCartItemQuantityPlus.setOnClickListener(v -> {
            cartItem.quantity += 1;
            cartItemDao.updateCartItem(cartItem);
            navController.navigate(R.id.action_cartFragment_self);
        });
        holder.buttonCartItemQuantitySubtract.setOnClickListener(v -> {
            if (cartItem.quantity > 1) {
                cartItem.quantity -= 1;
                cartItemDao.updateCartItem(cartItem);
                navController.navigate(R.id.action_cartFragment_self);
            } else {
                Toast.makeText(v.getContext(), "Quantity cannot less than 1", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCartItems.size();
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()   //Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number. Expected identity hash: db3c0ae21eaf52ebb7c432fc220cc6d0, found: 74d1741ddf4be43f64c6a8ae2afc19ed
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        productDao = db.productDao();
        cartItemDao = db.cartItemDao();
    }
}
