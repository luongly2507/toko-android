package com.app.toko.views.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.toko.databinding.ActivityForgotPasswordBinding;
import com.app.toko.viewmodels.ForgotPasswordViewModel;
import com.app.toko.views.fragments.AccountFragment;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    private ForgotPasswordViewModel forgotPasswordViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        setContentView(binding.getRoot());
        binding.buttonRetrievePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.editTextUserName.getText().toString();
                if (username.isBlank() ||
                        (username.trim().length() < 10) || username.trim().length() > 11) {
                    binding.textInputLayoutUserName.setError("Số điện thoại không đúng !");
                }
                else
                {
                    Intent intent = new Intent(ForgotPasswordActivity.this , VerificationActivity.class);
                    intent.putExtra("phone" , username);
                    intent.putExtra("fromActivity" , "ForgotPasswordActivity");
                    startActivity(intent);
                    finish();
                }
            }
        });
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}