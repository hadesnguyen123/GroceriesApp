package com.example.grocerietproject.fragments.shopping;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.grocerietproject.DAO.CategoryDao;
import com.example.grocerietproject.DAO.ProductDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.adapters.ProductAdapter;
import com.example.grocerietproject.entities.Product;
import com.example.grocerietproject.models.AdminProductRecycle;
import com.example.grocerietproject.models.ProductRecycle;
import com.example.grocerietproject.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    ProductDao productDao;
    CategoryDao categoryDao;
    ArrayList<ProductRecycle> newArrivalProducts;
    ArrayList<ProductRecycle> bestSellingProducts;

    RecyclerView productNewArrivalRecyclerView;
    RecyclerView productBestSellingRecyclerView;

    ProductAdapter newArrivalAdapter;
    ProductAdapter bestSellingAdapter;
    NavController navController;

    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productNewArrivalRecyclerView = view.findViewById(R.id.viewRecycleProductNewArrival);
        productBestSellingRecyclerView = view.findViewById(R.id.viewRecycleProductBestSelling);

        initRoomDatabase();
        navController = NavHostFragment.findNavController(this);

        // Khởi tạo các danh sách
        newArrivalProducts = new ArrayList<>();
        bestSellingProducts = new ArrayList<>();
        // Khởi tạo Adapter và truyền danh sách vào
        newArrivalAdapter = new ProductAdapter(newArrivalProducts, getContext(), navController);
        bestSellingAdapter = new ProductAdapter(bestSellingProducts, getContext(), navController);
        // Đặt Adapter và LayoutManager cho từng RecyclerView
        LinearLayoutManager newArrivalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        productNewArrivalRecyclerView.setAdapter(newArrivalAdapter);
        productNewArrivalRecyclerView.setLayoutManager(newArrivalLayoutManager);

        LinearLayoutManager bestSellingLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        productBestSellingRecyclerView.setAdapter(bestSellingAdapter);
        productBestSellingRecyclerView.setLayoutManager(bestSellingLayoutManager);

        loadProductNewArrivalFromDatabase();
        loadProductBestSellingFromDatabase();
    }

    private void loadProductBestSellingFromDatabase() {
        new Thread(() -> {
            List<Product> products = productDao.getBestSellers();
            for (Product product : products) {
                String categoryName = categoryDao.getCategoryNameById(product.categoryId);
                bestSellingProducts.add(new ProductRecycle(
                        product.productId,
                        categoryName,
                        product.name,
                        product.label,
                        product.description,
                        product.price,
                        product.weight,
                        product.amount
                ));
            }
            new Handler(Looper.getMainLooper()).post(() -> bestSellingAdapter.notifyDataSetChanged());
        }).start();
    }

    private void loadProductNewArrivalFromDatabase() {
        new Thread(() -> {
            List<Product> products = productDao.getNewArrival();
            for (Product product : products) {
                String categoryName = categoryDao.getCategoryNameById(product.categoryId);
                newArrivalProducts.add(new ProductRecycle(
                        product.productId,
                        categoryName,
                        product.name,
                        product.label,
                        product.description,
                        product.price,
                        product.weight,
                        product.amount
                ));
            }
            new Handler(Looper.getMainLooper()).post(() -> newArrivalAdapter.notifyDataSetChanged());
        }).start();
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()   //Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number. Expected identity hash: db3c0ae21eaf52ebb7c432fc220cc6d0, found: 74d1741ddf4be43f64c6a8ae2afc19ed
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
        categoryDao = db.categoryDao();
        productDao = db.productDao();
    }
}
