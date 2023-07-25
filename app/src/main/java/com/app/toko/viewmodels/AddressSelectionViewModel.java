package com.app.toko.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.toko.models.Contact;
import com.app.toko.repositories.ContactRepository;

import java.util.List;
import java.util.UUID;

public class AddressSelectionViewModel extends AndroidViewModel {

    private MutableLiveData<Contact> contact;
    public MutableLiveData<List<Contact>> mListMutableLiveData;
    private ContactRepository contactRepository;

    public AddressSelectionViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository(getApplication());
        mListMutableLiveData = new MutableLiveData<>();
    }

    public void initData(UUID userId, String token) {
        contactRepository.LoadContact(userId, token);
    }

    public ContactRepository getContactRepository() {
        return contactRepository;
    }

}
