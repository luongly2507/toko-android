package com.app.toko.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.models.User;
import com.app.toko.payload.request.AuthenticationRequest;
import com.app.toko.repositories.UserRepository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;



public class LoginViewModel extends AndroidViewModel {
    public MutableLiveData<String> passwordErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> phoneErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> loginErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();

    private LiveData<User> user;
        private UserRepository userRepository;

    public LoginViewModel(Application application){
        super(application);
        userRepository = new UserRepository(getApplication());
        user = userRepository.getUserLiveData();
    }

    public LiveData<User> getUser(){
        if (user == null) {
            user = new MutableLiveData<>();
        }
        return user;
    }

    public void onLoginClicked(){
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                        .phone(phone.getValue())
                        .password(password.getValue())
                        .build();
                if (!authenticationRequest.isPhoneValid()){
                    phoneErrorMessage.setValue("Vui lòng nhập đúng định dạng số điện thoại!");
                } else {
                    phoneErrorMessage.setValue(null);
                }
                if (!authenticationRequest.isPasswordLengthGreaterThan5()){
                    passwordErrorMessage.setValue("Mật khẩu phải lớn hơn 5 kí tự");
                } else {
                    passwordErrorMessage.setValue(null);
                }
                userRepository.authenticateUser(authenticationRequest);
            }
        }, 1000);
    }




}
