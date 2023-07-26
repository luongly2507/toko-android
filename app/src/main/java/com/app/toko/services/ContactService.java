package com.app.toko.services;

import com.app.toko.models.Contact;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContactService {

    @GET("api/v1/users/{id}/contacts/")
    Call<List<Contact>> getAllContact(@Path("id") UUID id,
                                      @Header("Authorization") String authHeader);

    @POST("api/v1/users/{id}/contacts/")
    Call<Contact> createNewContact(@Body Contact contact,
                                   @Path("id") UUID id,
                                   @Header("Authorization") String authHeader );

    @PUT("api/v1/users/{id}/contacts/{userID}/")
    Call<Contact> updateContact(@Body Contact contact,
                                   @Path("id") UUID id,
                                   @Header("Authorization") String authHeader,
                                   @Path("userID") UUID userID);

    @DELETE("api/v1/users/{id}/contacts/{userID}/")
    Call<Contact> deleteContact(@Path("id") UUID id,
                                   @Header("Authorization") String authHeader,
                                   @Path("userID") UUID userID);

    @GET("api/v1/users/{userId}/contacts/{contactId}/")
    Call<Contact> getContactById(@Path("userId") UUID userId , @Path("contactId") UUID contactId ,@Header("Authorization") String authHeader);
}
