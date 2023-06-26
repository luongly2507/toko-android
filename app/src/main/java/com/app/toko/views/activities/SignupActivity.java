package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.toko.databinding.ActivityLoginBinding;
import com.app.toko.databinding.ActivitySignupBinding;
import com.app.toko.viewmodels.SignupVewModel;

public class SignupActivity extends AppCompatActivity {
    private SignupVewModel signupVewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySignupBinding binding = ActivitySignupBinding.inflate(getLayoutInflater());
        signupVewModel = new ViewModelProvider(this).get(SignupVewModel.class);
        binding.setLifecycleOwner(this);
        binding.setSignupViewModel(signupVewModel);
        setContentView(binding.getRoot());

        binding.buttonBack.setOnClickListener(v -> onBackPressed());
        binding.buttonSignup.setOnClickListener(v->signupVewModel.registerUser());

    }
}