package com.app.toko.repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.Category;
import com.app.toko.models.User;
import com.app.toko.payload.request.AuthenticationRequest;
import com.app.toko.payload.response.AuthenticationResponse;
import com.app.toko.services.AuthenticationService;
import com.app.toko.services.UserService;
import com.app.toko.utils.ApiService;
import com.app.toko.views.activities.LoginActivity;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private AuthenticationService authenticationService;


    private UserService userService;
    private MutableLiveData<User> userMutableLiveData;

    private SharedPreferences sharedPreferences;
    public UserRepository(Application application){
        this.authenticationService = ApiService.getAuthenticationService();
        this.userService = ApiService.getUserService();
        this.userMutableLiveData = new MutableLiveData<>();
        this.sharedPreferences = application.getSharedPreferences("toko-preferences", Context.MODE_PRIVATE);
    }

    public void authenticateUser(AuthenticationRequest authenticationRequest){
        authenticationService.authenticate(authenticationRequest).enqueue(
                new Callback<AuthenticationResponse>() {
                    @Override
                    public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                         if (response.code() == 200) {
                             AuthenticationResponse authenticationResponse = response.body();
                             sharedPreferences.edit().putString("access_token",authenticationResponse.getAccessToken()).apply();
                             sharedPreferences.edit().putString("refresh_token",authenticationResponse.getRefreshToken()).apply();
                             sharedPreferences.edit().putString("user-id",authenticationResponse.getUserId().toString()).apply();
                            getUserDetail(authenticationResponse.getUserId(), "Bearer " + authenticationResponse.getAccessToken());
                         } else {
                             userMutableLiveData.postValue(null);
                         }
                    }

                    @Override
                    public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                        userMutableLiveData.postValue(null);
                    }
                }
        );
    }
    public void getUserDetail(UUID userId, String token){
        userService.getUserDetail(userId, token).enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200){
                            User user = response.body();
                            userMutableLiveData.postValue(user);
                        } else {
                            userMutableLiveData.postValue(null);
                        }

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        userMutableLiveData.postValue(null);
                    }
                }
        );
    }

    public LiveData<User> getUserLiveData(){
        return userMutableLiveData;
    }
}
