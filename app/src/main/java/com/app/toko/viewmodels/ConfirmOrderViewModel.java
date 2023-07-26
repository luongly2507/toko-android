package com.app.toko.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.toko.models.Contact;
import com.app.toko.payload.request.CreateOrderRequest;
import com.app.toko.repositories.ContactRepository;
import com.app.toko.repositories.UserRepository;

import java.util.List;
import java.util.UUID;

public class ConfirmOrderViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private ContactRepository contactRepository;
    private LiveData<Boolean> isSuccess;
    private LiveData<Contact> contactLiveData;
    public ConfirmOrderViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        contactRepository = new ContactRepository(application);
        isSuccess = userRepository.getIsSuccessful();
        contactLiveData = contactRepository.getContactMutableLiveData();
    }
    public void createOrder(UUID userId , CreateOrderRequest createOrderRequest , String token)
    {
        userRepository.createOrder(userId , createOrderRequest , token);
    }

    public LiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public void getContactById(UUID userId,UUID contactId , String token)
    {
        contactRepository.getContactById(userId , contactId ,"Bearer " + token);
    }

    public LiveData<Contact> getContactLiveData() {
        return contactLiveData;
    }
}
