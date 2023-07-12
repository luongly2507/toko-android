package com.app.toko.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewPasswordViewModel extends ViewModel {
    public MutableLiveData<String> mErrorPasswordMessage , mErrorRepeatMessage , password , repeatPassword;
    public NewPasswordViewModel() {
        mErrorPasswordMessage = new MutableLiveData<>();
        mErrorRepeatMessage = new MutableLiveData<>();
        password = new MutableLiveData<>();
        repeatPassword = new MutableLiveData<>();
    }

}
