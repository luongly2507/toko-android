package com.app.toko.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.toko.payload.request.UpdateCartItemRequest;
import com.app.toko.payload.response.BookResponse;
import com.app.toko.repositories.UserRepository;

import java.util.UUID;

public class BookDetailViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<Boolean> isSuccessful;

    public BookDetailViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        isSuccessful = userRepository.getIsSuccessful();

    }

    public LiveData<Boolean> getIsSuccessful() {
        return isSuccessful;
    }

    public void updateCartItem(UUID id , String token , UpdateCartItemRequest updateCartItemRequest)
    {
        userRepository.updateCartItem(id , token , updateCartItemRequest);
    }
}
