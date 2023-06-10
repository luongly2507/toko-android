package com.app.toko.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.models.User;

public class AccountViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    private MutableLiveData<User> user;

    public AccountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is account fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}