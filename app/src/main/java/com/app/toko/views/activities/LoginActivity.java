package com.app.toko.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.app.toko.R;
import com.app.toko.databinding.ActivityLoginBinding;
import com.app.toko.models.User;
import com.app.toko.viewmodels.LoginViewModel;
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setLoginViewModel(loginViewModel);
        setContentView(binding.getRoot());

        binding.buttonBack.setOnClickListener(v-> startActivity(new Intent(this,MainActivity.class)));
        binding.buttonLogin.setOnClickListener(v->loginViewModel.onLoginClicked());
        binding.textViewRegisterLink.setOnClickListener(view -> startActivity(new Intent(this,SignupActivity.class)));

        loginViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    onBackPressed();
                }

            }
        });
    }





}