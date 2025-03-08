package com.example.grocerietproject.fragments.shopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.grocerietproject.DAO.CartItemDao;
import com.example.grocerietproject.DAO.ProductDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.adapters.CartItemAdapter;
import com.example.grocerietproject.adapters.ProductAdapter;
import com.example.grocerietproject.adapters.ProductByCategoryAdapter;
import com.example.grocerietproject.entities.CartItem;
import com.example.grocerietproject.entities.Category;
import com.example.grocerietproject.entities.Product;
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.models.AdminAccountRecycle;
import com.example.grocerietproject.models.CartItemRecycle;
import com.example.grocerietproject.models.ProductRecycle;
import com.example.grocerietproject.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    CartItemDao cartItemDao;
    ProductDao productDao;
    ArrayList<CartItemRecycle> cartItemRecycles;
    RecyclerView cartItemRecycleView;
    CartItemAdapter cartItemAdapter;
    NavController navController;
    ImageView imgClearCart;
    Button buttonCheckOut;


    public CartFragment() {
        super(R.layout.fragment_cart);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartItemRecycleView = view.findViewById(R.id.viewRecycleCartItem);
        imgClearCart = view.findViewById(R.id.imgClearCart);
        buttonCheckOut = view.findViewById(R.id.buttonCheckOut);

        initRoomDatabase();
        loadCartItemsFromDatabase();
        navController = NavHostFragment.findNavController(this);

        cartItemRecycles = new ArrayList<CartItemRecycle>();
        navController = NavHostFragment.findNavController(this);
        cartItemAdapter = new CartItemAdapter(cartItemRecycles, getContext(), navController);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        cartItemRecycleView.setAdapter(cartItemAdapter);
        cartItemRecycleView.setLayoutManager(linearLayoutManager);
        imgClearCart.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure to delete this Cart ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new Thread(() -> {
                            SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("AccountPreference", Context.MODE_PRIVATE);
                            int userId = pref.getInt("userId", 0);
                            if (userId != 0) {
                                cartItemDao.clearCartByUserId(userId);
                            }
                            new Handler(Looper.getMainLooper()).post(() -> {
                                Toast.makeText(getContext(), "Delete Success", Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(this)
                                        .navigate(R.id.action_cartFragment_self);
                            });
                        }).start();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        buttonCheckOut.setOnClickListener(v -> {
            SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("AccountPreference", Context.MODE_PRIVATE);
            int userId = pref.getInt("userId", 1);
            List<CartItem> cartItems = cartItemDao.getCartItemsByUserId(userId);
            if(cartItems.size() > 0){
                for(CartItem cartItem: cartItems){
                    Product product = productDao.getProductById(cartItem.productId);
                    if(cartItem.quantity > product.amount){
                        Toast.makeText(getContext(), "Cannot have enough amount product", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        new Thread(() -> {
                            product.amount = product.amount - cartItem.quantity;
                            productDao.update(product);
                            cartItemDao.clearCartByUserId(userId);
                            new Handler(Looper.getMainLooper()).post(() -> {
                                Toast.makeText(getContext(), "Delete Success", Toast.LENGTH_SHORT).show();
                                NavHostFragment.findNavController(this)
                                        .navigate(R.id.action_cartFragment_self);
                            });
                        }).start();
                    }
                }
                Toast.makeText(getContext(), "Checkout success", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Do not have any item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartItemsFromDatabase() {
        new Thread(() -> {
            SharedPreferences pref = getContext().getApplicationContext().getSharedPreferences("AccountPreference", Context.MODE_PRIVATE);
            int userId = pref.getInt("userId", 0);
            List<CartItem> cartItems = cartItemDao.getCartItemsByUserId(userId);
            for (CartItem cartItem : cartItems) {
                Product productCartItem = productDao.getProductById(cartItem.productId);
                cartItemRecycles.add(new CartItemRecycle(
                        cartItem.cartItemId,
                        cartItem.productId,
                        cartItem.quantity,
                        productCartItem.name,
                        productCartItem.weight,
                        productCartItem.price * cartItem.quantity
                ));
            }
            new Handler(Looper.getMainLooper()).post(() -> cartItemAdapter.notifyDataSetChanged());
        }).start();
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()   //Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number. Expected identity hash: db3c0ae21eaf52ebb7c432fc220cc6d0, found: 74d1741ddf4be43f64c6a8ae2afc19ed
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        cartItemDao = db.cartItemDao();
        productDao = db.productDao();
    }
}
