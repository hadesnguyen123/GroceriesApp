package com.example.grocerietproject.fragments.loginRegister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.grocerietproject.DAO.CategoryDao;
import com.example.grocerietproject.DAO.ProductDao;
import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.activities.ShoppingActivity;
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.util.AppDatabase;

public class LoginFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    EditText edLoginEmail;
    EditText edLoginPassword;
    Button buttonLoginLogin;
    TextView tvLoginNavToRegister;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonLoginLogin = view.findViewById(R.id.buttonLoginLogin);
        tvLoginNavToRegister = view.findViewById(R.id.tvLoginNavToRegister);
        edLoginEmail = view.findViewById(R.id.edLoginEmail);
        edLoginPassword = view.findViewById(R.id.edLoginPassword);

        tvLoginNavToRegister.setOnClickListener(v -> {
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_loginFragment_to_registerFragment);
        });

        initRoomDatabase();

        buttonLoginLogin.setOnClickListener(v -> {
            String email = edLoginEmail.getText().toString();
            String password = edLoginPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter full information", Toast.LENGTH_SHORT).show();
                return;
            }
            User userLogin = userDao.login(edLoginEmail.getText().toString(),
                    edLoginPassword.getText().toString());
            if (userLogin != null) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getContext(), "Login successfully !", Toast.LENGTH_SHORT).show();
                    //lưu trên local
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AccountPreference", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", userLogin.email);
                    editor.putString("password", userLogin.passWord);
                    editor.putInt("userId", userLogin.userId);
                    editor.apply();

                    Intent intent = new Intent(getActivity(), ShoppingActivity.class);
                    startActivity(intent);
                });
            } else {
                Toast.makeText(getContext(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();
    }


}
