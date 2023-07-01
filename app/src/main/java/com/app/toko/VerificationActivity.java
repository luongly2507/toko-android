package com.app.toko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.toko.databinding.ActivityVerificationBinding;
import com.app.toko.viewmodels.VerificationViewModel;

public class VerificationActivity extends AppCompatActivity {
    private VerificationViewModel verificationViewModel;
    private ActivityVerificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        verificationViewModel = new ViewModelProvider(this).get(VerificationViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setVerificationViewModel(verificationViewModel);

        setContentView(binding.getRoot());

        binding.buttonVerify.setOnClickListener(view -> {
            //Change button
            binding.buttonVerify.setText("XÁC THỰC");
            //SetOnclickListener
            binding.buttonVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Verify
                    final String generateOTP = binding.editTextOTP1.getText().toString() +
                            binding.editTextOTP2.getText().toString() +
                            binding.editTextOTP3.getText().toString() +
                            binding.editTextOTP4.getText().toString() +
                            binding.editTextOTP5.getText().toString() +
                            binding.editTextOTP6.getText().toString();
                    if (generateOTP.length() == 6) {
                        //Verification success handler
                        try {
                            verificationViewModel.signIn();
                        }
                        catch (Exception e)
                        {
                            System.out.print("Lỗi: " + e);
                            Toast.makeText(VerificationActivity.this, "Chưa thể gửi OTP, không thể xác thực!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        Toast.makeText(VerificationActivity.this, "Hãy nhập đầy đủ mã OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

    }
}