package com.example.grocerietproject.fragments.loginRegister;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.grocerietproject.DAO.UserDao;
import com.example.grocerietproject.R;
import com.example.grocerietproject.entities.User;
import com.example.grocerietproject.util.AppDatabase;

public class RegisterFragment extends Fragment {
    AppDatabase db;
    UserDao userDao;
    Button buttonRegister;
    TextView tvRegisterBackToLogin;
    TextView edRegisterFullName;
    TextView edRegisterEmail;
    TextView edRegisterPassword;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonRegister = view.findViewById(R.id.buttonRegisterRegister);
        tvRegisterBackToLogin = view.findViewById(R.id.tvRegisterBackToLogin);
        edRegisterFullName = view.findViewById(R.id.edRegisterFullName);
        edRegisterEmail = view.findViewById(R.id.edRegisterEmail);
        edRegisterPassword = view.findViewById(R.id.edRegisterPassword);


        initRoomDatabase();

        tvRegisterBackToLogin.setOnClickListener(v -> {
            NavHostFragment.findNavController(RegisterFragment.this)
                    .navigate(R.id.action_registerFragment_to_loginFragment);
        });

        buttonRegister.setOnClickListener(v -> {
            String fullname = edRegisterFullName.getText().toString();
            String password = edRegisterPassword.getText().toString();
            String email = edRegisterEmail.getText().toString();
            if (fullname.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(getContext(), "Please enter full information", Toast.LENGTH_SHORT).show();
                return;
            }
            //check exist
            User userExist = userDao.getEmailById(email);
            if (userExist != null) {
                Toast.makeText(getContext(), "Email is already exist", Toast.LENGTH_SHORT).show();
                return;
            }
            User newUser = new User();
            newUser.role = 1;
            newUser.fullName = fullname;
            newUser.email = email;
            newUser.passWord = password;
            new Thread(() -> {
                userDao.register(newUser);
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getContext(), "Register successfully !", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(RegisterFragment.this)
                            .navigate(R.id.action_registerFragment_to_loginFragment);
                });
            }).start();

        });
    }

    private void initRoomDatabase() {
        db = Room.databaseBuilder(requireContext().getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();   //=> tăng version và thêm fallbackToDestructiveMigration
        userDao = db.userDao();

    }
}
