package com.app.toko.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.CartItem;
import com.app.toko.repositories.UserRepository;

import java.util.List;
import java.util.UUID;

public class CartViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private MutableLiveData<List<CartItem>> cartItemsLiveData;

    public CartViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        cartItemsLiveData = userRepository.getCartItemsLiveData();
    }

    public MutableLiveData<List<CartItem>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }

    public void fetchCartItems(UUID userId, String token) {
        userRepository.getUserCartItems(userId, token);
    }
}
