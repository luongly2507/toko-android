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
        binding.buttonSignup.setOnClickListener(v -> signupVewModel.registerUser());

        // Lắng nghe sự thay đổi trong các MutableLiveData để kiểm tra tính hợp lệ
        signupVewModel.firstname.observe(this, firstname -> {
            if (firstname != null && firstname.trim().isEmpty()) {
                signupVewModel.firstnameErrorMessage.setValue("Hãy nhập tên của bạn!");
            } else {
                signupVewModel.firstnameErrorMessage.setValue(null);
            }
        });

        signupVewModel.lastname.observe(this, lastname -> {
            if (lastname != null && lastname.trim().isEmpty()) {
                signupVewModel.lastnameErrorMessage.setValue("Hãy nhập họ của bạn!");
            } else {
                signupVewModel.lastnameErrorMessage.setValue(null);
            }
        });

        signupVewModel.email.observe(this, email -> {
            if (email != null && (email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                signupVewModel.emailErrorMessage.setValue("Hãy nhập đúng định dạng email!");
            } else {
                signupVewModel.emailErrorMessage.setValue(null);
            }
        });

        signupVewModel.password.observe(this, password -> {
            if (password != null && (password.trim().isEmpty() || password.length() < 6)) {
                signupVewModel.passwordErrorMessage.setValue("Mật khẩu phải có ít nhất 6 ký tự!");
            } else {
                signupVewModel.passwordErrorMessage.setValue(null);
            }
        });

        signupVewModel.phone.observe(this, phone -> {
            if (phone != null && phone.length() > 11) {
                signupVewModel.phoneErrorMessage.setValue("Số điện thoại quá dài");
            } else {
                signupVewModel.phoneErrorMessage.setValue(null);
            }
        });
    }
}
