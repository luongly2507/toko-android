package com.app.toko.services;

import com.app.toko.models.Contact;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ContactService {

    @GET("api/v1/users/5f4a6b44-d051-4fca-80b6-f4f0ec408b0f/contacts/")
    Call<ArrayList<Contact>> getAllContact();

    @POST("api/v1/users/5f4a6b44-d051-4fca-80b6-f4f0ec408b0f/contacts/")
    Call<Contact> createNewContact(@Body Contact contact);

}
