package com.app.toko.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.app.toko.R;
import com.app.toko.databinding.ActivityVerificationBinding;
import com.app.toko.viewmodels.VerificationViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    private VerificationViewModel verificationViewModel;
    private ActivityVerificationBinding binding;
    //Test phone number : +84999999911
    //Test verification code: 111111
    private String phoneNumber = "+84 375546870";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    //Enable resend after 60s
    boolean resendEnable = false;
    //Resend time in seconds
    private Long resendTime = 60L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVerificationBinding.inflate(getLayoutInflater());

        verificationViewModel = new ViewModelProvider(this).get(VerificationViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setVerificationViewModel(verificationViewModel);

        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent.hasExtra("phone")) {
            String phone = intent.getStringExtra("phone");
            phoneNumber = verificationViewModel.formatPhoneNumber(phone);
        }
        else
        {
            Toast.makeText(this, "Chưa thể gửi OTP, không thể xác thực!", Toast.LENGTH_SHORT).show();
            return;
        }

        //set string phone number
        binding.textViewPhoneNumber.setText(phoneNumber);
        //set EditText otp text changed listener
        setupETOTP();
        //open keyboard at opt1 as default
        showKeyboard(binding.editTextOTP1);
        //start resend timer countdown
        startCountdownTimer();

        binding.buttonResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendEnable){
                    //OTP sending handler
                    sendOTP(phoneNumber, true);
                    //start resend countdown
                    startCountdownTimer();
                }
            }
        });

        binding.buttonVerify.setOnClickListener(view -> {
            //Send otp
            sendOTP(phoneNumber, false);
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
                            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCode, generateOTP);
                            signIn(phoneAuthCredential);
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

    private void showKeyboard(EditText ETOTP){
        ETOTP.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(ETOTP, inputMethodManager.SHOW_IMPLICIT);
    }
    private void startCountdownTimer(){
        resendEnable = false;

        binding.buttonResend.setTextColor(Color.BLACK);

        new CountDownTimer(resendTime * 1000, 1000){

            @Override
            public void onTick(long l) {
                binding.buttonResend.setText("Gửi lại sau (" + (l / 1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnable = true;
                binding.buttonResend.setText("Gửi lại");
                binding.buttonResend.setTextColor(getResources().getColor(R.color.deepblue));
            }
        }.start();
    }
    private void setupETOTP(){
        binding.editTextOTP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    showKeyboard(binding.editTextOTP2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.editTextOTP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    showKeyboard(binding.editTextOTP3);
                }
                else {
                    showKeyboard(binding.editTextOTP1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.editTextOTP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    showKeyboard(binding.editTextOTP4);
                }
                else {
                    showKeyboard(binding.editTextOTP2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.editTextOTP4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    showKeyboard(binding.editTextOTP5);
                }
                else {
                    showKeyboard(binding.editTextOTP3);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.editTextOTP5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    showKeyboard(binding.editTextOTP6);
                }
                else {
                    showKeyboard(binding.editTextOTP4);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.editTextOTP6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()){
                    showKeyboard(binding.editTextOTP5);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void sendOTP(String phoneNumber, boolean isResend){
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(resendTime, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                //signIn(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerificationActivity.this, "Gửi OTP thất bại! Hãy kiểm tra lại số điện thoại", Toast.LENGTH_SHORT).show();
                                System.out.print("Lỗi: " + e.toString());
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                Toast.makeText(VerificationActivity.this, "Đã gửi mã OTP!", Toast.LENGTH_SHORT).show();
                            }
                        });
        if (isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }
    private void signIn(PhoneAuthCredential phoneAuthCredential){
        //sign up success and move to next activity
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Return to SignupActivity
                    switch (getIntent().getStringExtra("fromActivity"))
                    {
                        case "SignupActivity":
                            Intent resultIntent = new Intent();
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish(); // Đóng Activity và quay về màn hình trước đó
                            break;
                        case "ForgotPasswordActivity":
                            Intent toNewPassIntent = new Intent(VerificationActivity.this , NewPasswordActitivy.class);
                            toNewPassIntent.putExtra("phone" , getIntent().getStringExtra("phone"));
                            startActivity(toNewPassIntent);
                            finish();
                            break;
                    }

                }else {
                    Toast.makeText(VerificationActivity.this, "Xác thực OTP thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}