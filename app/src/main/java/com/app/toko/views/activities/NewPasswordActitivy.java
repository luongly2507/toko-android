package com.app.toko.views.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.toko.R;
import com.app.toko.databinding.ActivityNewPasswordActitivyBinding;
import com.app.toko.viewmodels.NewPasswordViewModel;

import java.util.Objects;

public class NewPasswordActitivy extends AppCompatActivity {
    private NewPasswordViewModel newPasswordViewModel;
    private ActivityNewPasswordActitivyBinding binding;
    private AlertDialog.Builder dialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewPasswordActitivyBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
        dialogBuilder = new AlertDialog.Builder(this);
        newPasswordViewModel = new ViewModelProvider(this).get(NewPasswordViewModel.class);
        binding.setNewPasswordViewModel(newPasswordViewModel);
        newPasswordViewModel.password.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String password) {
                if (password != null && (password.trim().isEmpty() || password.length() < 6)) {
                    newPasswordViewModel.mErrorPasswordMessage.setValue("Mật khẩu phải có ít nhất 6 ký tự!");
                } else {
                    newPasswordViewModel.mErrorPasswordMessage.setValue(null);
                }
                if(!Objects.equals(password, newPasswordViewModel.repeatPassword.getValue()))
                {
                    newPasswordViewModel.mErrorRepeatMessage.setValue("Mật khẩu không khớp với mật khẩu mới");
                }
                else newPasswordViewModel.mErrorRepeatMessage.setValue(null);
            }
        });
        newPasswordViewModel.repeatPassword.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String repeatPass) {
                if(!Objects.equals(repeatPass, newPasswordViewModel.password.getValue()))
                {
                    newPasswordViewModel.mErrorRepeatMessage.setValue("Mật khẩu không khớp với mật khẩu mới");
                }
                else newPasswordViewModel.mErrorRepeatMessage.setValue(null);
            }
        });
        binding.buttonRetrievePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(newPasswordViewModel.password.getValue(), newPasswordViewModel.repeatPassword.getValue()))
                {
                    //update ....
                    Toast.makeText(NewPasswordActitivy.this, "chức năng cập nhật user", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(NewPasswordActitivy.this, "Mật khẩu không khớp !", Toast.LENGTH_SHORT).show();
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                dialogBuilder.setTitle("Cảnh báo")
                        .setMessage("Nếu bạn thoát ra thì sẽ không thể đổi lại mật khẩu ! \nBạn có muốn thoát ra ?")
                        .setCancelable(true)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(NewPasswordActitivy.this , LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}