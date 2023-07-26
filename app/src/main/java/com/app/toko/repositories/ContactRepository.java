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
                    Toast.makeText(application, "Thêm địa chỉ thành công!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Toast.makeText(application, "Lỗi kết nối mạng !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UpdateContact(MutableLiveData<Contact> contact, UUID userId, String token, UUID userIdUpadate){
        contactService.updateContact(contact.getValue(), userId, token, userIdUpadate).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                Log.d("Contact","Update API Contact Success");

                Contact contactResult = response.body();
                if (contactResult != null){
                    Log.d("Contact",contactResult.toString());
                    Toast.makeText(application, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Toast.makeText(application, "Lỗi kết nối mạng !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void DeleteContact(UUID userId, String token, UUID userIdDelete){
        contactService.deleteContact(userId, token, userIdDelete).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                Log.d("Contact","Delete API Contact Success");
                if(response.isSuccessful())
                {
                    Toast.makeText(application, "Xóa thành công !", Toast.LENGTH_SHORT).show();

                }
                else if (response.code() == 404)
                {
                    Toast.makeText(application, "Địa chỉ này hiện đã/đang đặt hàng \n Vui lòng không được xóa !", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.d("Contact","Delete API Contact Failure");

            }
        });
    }
}
