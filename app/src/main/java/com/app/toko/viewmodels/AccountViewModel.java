package com.app.toko.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.models.User;
import com.app.toko.repositories.AddressRepository;
import com.app.toko.repositories.UserRepository;

import java.util.UUID;

public class AccountViewModel extends AndroidViewModel {


    private LiveData<User> user;
    private UserRepository userRepository;

    public AccountViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
        user = userRepository.getUserLiveData();

    }
    public void getUserDetail(UUID userID , String token)
    {
        userRepository.getUserDetail(userID ,"Bearer " + token);
    }
    public LiveData<User> getUser(){return user;};

}