package com.app.toko.views.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedCallback;


import com.app.toko.R;
import com.app.toko.databinding.ActivityLoginBinding;
import com.app.toko.models.User;
import com.app.toko.viewmodels.LoginViewModel;
import com.app.toko.views.fragments.AccountFragment;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setLoginViewModel(loginViewModel);
        setContentView(binding.getRoot());
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(sharedPreferences.getString("access_token" , null) == null)
                {
                    Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                    intent.putExtra("toFrag" , "account");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        binding.buttonBack.setOnClickListener(v-> startActivity(new Intent(this,MainActivity.class)));
        binding.buttonLogin.setOnClickListener(v->loginViewModel.onLoginClicked());
        binding.textViewRegisterLink.setOnClickListener(view -> startActivity(new Intent(this,SignupActivity.class)));
        sharedPreferences = getSharedPreferences("toko-preferences", Context.MODE_PRIVATE);
        loginViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    //System.out.println("Da co doi tuong" + user.toString());
                    onBackPressed();
                }

            }
        });
    }



}