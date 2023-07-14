package com.app.toko.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.repositories.UserRepository;

public class NewPasswordViewModel extends AndroidViewModel {
    public MutableLiveData<String> mErrorPasswordMessage , mErrorRepeatMessage , password , repeatPassword;
    private UserRepository userRepository;
    public NewPasswordViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
        mErrorPasswordMessage = new MutableLiveData<>();
        mErrorRepeatMessage = new MutableLiveData<>();
        password = new MutableLiveData<>();
        repeatPassword = new MutableLiveData<>();
    }

    public void updatePassword(String phone , String password)
    {

        userRepository.updateUserPassword(phone, password);
    }

}
