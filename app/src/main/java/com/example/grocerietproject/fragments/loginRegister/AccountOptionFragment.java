package com.example.grocerietproject.fragments.loginRegister;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.grocerietproject.R;

public class AccountOptionFragment extends Fragment {
    public AccountOptionFragment() {
        super(R.layout.fragment_account_options);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button loginOptionButton = view.findViewById(R.id.buttonLoginAccountOptions);
        Button registerOptionButton = view.findViewById(R.id.buttonRegisterAccountOptions);
        loginOptionButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(AccountOptionFragment.this)
                    .navigate(R.id.action_accountOptionFragment_to_loginFragment);
        });

        registerOptionButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(AccountOptionFragment.this)
                    .navigate(R.id.action_accountOptionFragment_to_registerFragment);
        });
    }
}
