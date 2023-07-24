package com.app.toko.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.Contact;
import com.app.toko.services.ContactService;
import com.app.toko.utils.ApiService;

import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactRepository {
    private ContactService contactService;
    private Application application;
    private Context appContext;

    private MutableLiveData<ArrayList<Contact>> contactMutableLiveData;

    public ContactRepository(Application application) {
        this.contactService = ApiService.getContactService();
        this.application = application;
        this.appContext = application.getApplicationContext();
        this.contactMutableLiveData = new MutableLiveData<>();
    }

    public void LoadContact(UUID userId, String token){
        contactService.getAllContact(userId, token).enqueue(new Callback<ArrayList<Contact>>() {
            @Override
            public void onResponse(Call<ArrayList<Contact>> call, Response<ArrayList<Contact>> response) {
                Log.d("Contact","Get API Contact Success" );

                ArrayList<Contact> contacts = response.body();
                contactMutableLiveData.postValue(contacts);
            }

            @Override
            public void onFailure(Call<ArrayList<Contact>> call, Throwable t) {
                Log.d("Contact","Get API Contact Failure" );
                contactMutableLiveData.postValue(null);
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
