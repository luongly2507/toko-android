package com.app.toko.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.repositories.UserRepository;

public class ForgotPasswordViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    public LiveData<Boolean> isExistUser;
    public ForgotPasswordViewModel(Application application)
    {
        super(application);
        userRepository = new UserRepository(application);
        isExistUser = userRepository.getIsExistUser();
    }
    public void getUser(String phone)
    {
       userRepository.isExistUserByPhone(phone);
    }

}
