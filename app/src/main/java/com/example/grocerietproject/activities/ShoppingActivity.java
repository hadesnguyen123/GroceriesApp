package com.example.grocerietproject.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.grocerietproject.R;
import com.example.grocerietproject.fragments.loginRegister.IntroductionFragment;
import com.example.grocerietproject.fragments.shopping.HomeFragment;
import com.example.grocerietproject.fragments.shopping.ProfileFragment;
import com.example.grocerietproject.fragments.shopping.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShoppingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Chuyển đổi giữa các fragment
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        NavController navController = Navigation.findNavController(this, R.id.shoppingHostFragment);

        // Set up bottom navigation with navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}