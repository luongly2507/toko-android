package com.app.toko.viewmodels;

import android.app.Application;
import android.os.Handler;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.User;
import com.app.toko.payload.request.AuthenticationRequest;
import com.app.toko.repositories.UserRepository;

public class SignupVewModel extends AndroidViewModel {
    public MutableLiveData<String> passwordErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> phoneErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> emailErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> firstnameErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> lastnameErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> genderErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> loginErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> firstname = new MutableLiveData<>();
    public MutableLiveData<String> lastname = new MutableLiveData<>();
    public MutableLiveData<Integer> gender = new MutableLiveData<>();

    private LiveData<User> user;
    private UserRepository userRepository;

    public SignupVewModel(Application application){
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

}
