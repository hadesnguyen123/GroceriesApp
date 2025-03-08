package com.example.grocerietproject.fragments.shopping;

import android.os.Bundle;
import android.view.View;

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
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.adapters.CategoryAdapter;
import com.example.grocerietproject.adapters.ProductAdapter;
import com.example.grocerietproject.entities.Category;
import com.example.grocerietproject.models.CategoryRecycle;
import com.example.grocerietproject.models.ProductRecycle;
import com.example.grocerietproject.util.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    CategoryDao categoryDao;
    ArrayList<CategoryRecycle> categoryRecycles;
    RecyclerView categoryRecyclerView;
    CategoryAdapter categoryAdapter;
    NavController navController;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //UI
        categoryRecyclerView = view.findViewById(R.id.viewRecycleCategoryList);
        categoryRecycles = new ArrayList<CategoryRecycle>();

        initRoomDatabase();
        loadCategorysFromDatabase();

        // Sử dụng GridLayoutManager với 2 cột để hiển thị 2 sản phẩm trên một hàng
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        categoryAdapter = new CategoryAdapter(categoryRecycles, getContext());
        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onItemClick(CategoryRecycle category) {
                Bundle bundle = new Bundle();
                bundle.putString("categoryName", category.getName());
                bundle.putInt("categoryId", category.getCategoryId());
                navController = NavHostFragment.findNavController(SearchFragment.this);
                navController.navigate(R.id.action_searchFragment_to_productByCategoryFragment, bundle);
            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void loadCategorysFromDatabase() {
        List<Category> categories = categoryDao.getAllCategories();
        for (Category category : categories){
            categoryRecycles.add(new CategoryRecycle(category.categoryId,"", category.categoryName));
        }
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
        categoryDao = db.categoryDao();
    }
}
