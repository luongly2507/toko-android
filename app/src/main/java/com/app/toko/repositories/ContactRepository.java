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

    public void LoadContact(){
        contactService.getAllContact().enqueue(new Callback<ArrayList<Contact>>() {
            @Override
            public void onResponse(Call<ArrayList<Contact>> call, Response<ArrayList<Contact>> response) {
                Toast.makeText(appContext, "Get API Contact Success", Toast.LENGTH_SHORT).show();

                ArrayList<Contact> contacts = response.body();
                contactMutableLiveData.postValue(contacts);
            }

            @Override
            public void onFailure(Call<ArrayList<Contact>> call, Throwable t) {
                Toast.makeText(appContext, "Get API Contact Failure", Toast.LENGTH_SHORT).show();
                contactMutableLiveData.postValue(null);
            }
        });
    }

    public void RegisterContact(MutableLiveData<Contact> contact){

        contactService.createNewContact(contact.getValue()).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                Toast.makeText(appContext, "Post API Contact Success", Toast.LENGTH_SHORT).show();

                Contact contactResult = response.body();
                if (contactResult != null){
                    Log.e("Post Contact",contactResult.toString());
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Toast.makeText(appContext, "Post API Contact Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
