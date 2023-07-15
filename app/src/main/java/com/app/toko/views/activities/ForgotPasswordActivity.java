package com.app.toko.views.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.toko.databinding.ActivityForgotPasswordBinding;
import com.app.toko.viewmodels.ForgotPasswordViewModel;
import com.app.toko.views.fragments.AccountFragment;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        setContentView(binding.getRoot());
        forgotPasswordViewModel.isExistUser.observe(ForgotPasswordActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExist) {
                if(isExist)
                {
                    if(username != null && !username.isBlank())
                    {
                        Intent intent = new Intent(ForgotPasswordActivity.this , VerificationActivity.class);
                        intent.putExtra("phone" , username);
                        intent.putExtra("fromActivity" , "ForgotPasswordActivity");
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Số điện thoại hiện chưa được đăng kí!!", Toast.LENGTH_LONG).show();
                }

            }
        });
        binding.buttonRetrievePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = binding.editTextUserName.getText().toString();
                if (username.isBlank() ||
                        (username.trim().length() < 10) || username.trim().length() > 11) {
                    binding.textInputLayoutUserName.setError("Số điện thoại không đúng !");
                }
                else
                {
                    forgotPasswordViewModel.getUser(username);
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