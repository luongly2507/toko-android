package com.app.toko.viewmodels;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.app.toko.views.activities.SignupSuccessActivity;

public class VerificationViewModel extends AndroidViewModel {
    public VerificationViewModel(@NonNull Application application) {
        super(application);
    }

    public void signIn(){
        //sign up success and move to next activity
        Intent intent = new Intent(getApplication().getApplicationContext(), SignupSuccessActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplication().startActivity(intent);
    }

    public String formatPhoneNumber(String phoneNumber) {
        // Xóa khoảng trắng và ký tự đầu của số điện thoại
        String formattedNumber = phoneNumber.trim().substring(1);
        // Ghép chuỗi "+84 " vào trước số điện thoại
        formattedNumber = "+84 " + formattedNumber;
        return formattedNumber;
    }
}
