package com.app.toko.services;

import com.app.toko.models.Contact;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContactService {

    @GET("api/v1/users/{id}/contacts/")
    Call<ArrayList<Contact>> getAllContact(@Path("id") UUID id, @Header("Authorization") String authHeader);

    @POST("api/v1/users/{id}/contacts/")
    Call<Contact> createNewContact(@Body Contact contact, @Path("id") UUID id, @Header("Authorization") String authHeader );

}
