package com.app.toko.services;

import com.app.toko.payload.request.AuthenticationRequest;
import com.app.toko.payload.response.AuthenticationResponse;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;

public interface AuthenticationService {

    @POST("/api/v1/auth/authenticate")
    Call<AuthenticationResponse> authenticate(@Body AuthenticationRequest authenticationRequest);

}
