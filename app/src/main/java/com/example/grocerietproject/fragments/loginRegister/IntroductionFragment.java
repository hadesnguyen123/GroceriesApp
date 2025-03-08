package com.example.grocerietproject.fragments.loginRegister;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.grocerietproject.R;

public class IntroductionFragment extends Fragment {
    public IntroductionFragment(){
        super(R.layout.fragment_introduction); //gán layout cho introFragment
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button getStartedButton = view.findViewById(R.id.buttonIntroGetStarted);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dùng nav controller để chuyển Account OptionFragment
                NavController navController = NavHostFragment.findNavController(IntroductionFragment.this);
                navController.navigate(R.id.action_introductionFragment_to_accountOptionFragment);
            }
        });
    }
}
