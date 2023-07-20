package com.app.toko.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.payload.response.CartResponse;
import com.app.toko.repositories.UserRepository;

import java.util.List;
import java.util.UUID;

public class CartViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private MutableLiveData<List<CartResponse>> cartResponsesLiveData;

    public CartViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        cartResponsesLiveData = userRepository.getCartResponsesLiveData();
    }

    public MutableLiveData<List<CartResponse>> getCartResponsesLiveData() {
        return cartResponsesLiveData;
    }

    public void fetchCartItems(UUID userId, String token) {
        userRepository.getUserCartItems(userId, token);
    }
}
