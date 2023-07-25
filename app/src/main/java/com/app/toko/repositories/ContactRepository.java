package com.app.toko.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.Contact;
import com.app.toko.services.ContactService;
import com.app.toko.utils.ApiService;


import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactRepository {
    private ContactService contactService;
    private Application application;
    private Context appContext;

    private MutableLiveData<List<Contact>> mListMutableLiveData;

    public ContactRepository(Application application) {
        this.contactService = ApiService.getContactService();
        this.application = application;
        this.appContext = application.getApplicationContext();
        this.mListMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Contact>> getListMutableLiveData() {
        return mListMutableLiveData;
    }

    public void LoadContact(UUID userId, String token){
        contactService.getAllContact(userId, token).enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                Log.d("Contact","Get API Contact Success" );

                List<Contact> contacts = response.body();
                mListMutableLiveData.postValue(contacts);
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.d("Contact","Get API Contact Failure" );
                mListMutableLiveData.postValue(null);
            }
        });
    }

    public void RegisterContact(MutableLiveData<Contact> contact, UUID userId, String token){

        contactService.createNewContact(contact.getValue(), userId, token).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                Log.d("Contact","Post API Contact Success");

                Contact contactResult = response.body();
                if (contactResult != null){
                    Log.d("Contact",contactResult.toString());
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.d("Contact","Post API Contact Failure");
            }
        });
    }
}
