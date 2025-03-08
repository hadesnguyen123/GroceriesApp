package com.example.grocerietproject.fragments.shopping;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.grocerietproject.DAO.CategoryDao;
import com.example.grocerietproject.DAO.ProductDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.adapters.ProductAdapter;
import com.example.grocerietproject.adapters.ProductByCategoryAdapter;
import com.example.grocerietproject.entities.Product;
import com.example.grocerietproject.models.ProductRecycle;
import com.example.grocerietproject.util.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProductByCategoryFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    CategoryDao categoryDao;
    ProductDao productDao;
    ArrayList<ProductRecycle> productRecycles;
    RecyclerView productRecyclerView;
    ProductByCategoryAdapter productAdapter;
    NavController navController;

    public ProductByCategoryFragment() {
        super(R.layout.fragment_products_by_category);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Danh sách sản phẩm
        productRecyclerView = view.findViewById(R.id.viewRecycleProductByCategoryList);
        productRecycles = new ArrayList<ProductRecycle>();

        navController = NavHostFragment.findNavController(this);
        productAdapter = new ProductByCategoryAdapter(productRecycles, getContext(), navController);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        productRecyclerView.setAdapter(productAdapter);
        productRecyclerView.setLayoutManager(gridLayoutManager);

        initRoomDatabase();

        //set giá trị vào view
        TextView categoryDetailText = view.findViewById(R.id.tvProductsByCategoryCateName);
        ImageView backButton = view.findViewById(R.id.imgProductsByCategoryBack);
        Bundle args = getArguments();
        if (args != null) {
            String categoryName = args.getString("categoryName");
            int categoryId = args.getInt("categoryId");
            loadCategoriesFromDatabase(categoryId);
            categoryDetailText.setText(categoryName);
        }
        backButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_productByCategoryFragment_to_searchFragment);
        });
    }

    private void loadCategoriesFromDatabase(int categoryId) {
        Log.d("id", String.valueOf(categoryId));
        List<Product> products = productDao.getProductByCategoryId(categoryId);
        for (Product product : products){
            String categoryName = categoryDao.getCategoryNameById(product.categoryId);
            productRecycles.add(new ProductRecycle(
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
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
        categoryDao = db.categoryDao();
        productDao = db.productDao();
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
