package com.app.toko.repositories;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.User;
import com.app.toko.payload.request.AuthenticationRequest;
import com.app.toko.payload.response.AuthenticationResponse;
import com.app.toko.services.AuthenticationService;
import com.app.toko.services.UserService;
import com.app.toko.utils.ApiService;
import com.app.toko.views.activities.SignupSuccessActivity;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private AuthenticationService authenticationService;
    private UserService userService;
    private MutableLiveData<User> userMutableLiveData;
    private SharedPreferences sharedPreferences;
    private Application application;

    public UserRepository(Application application){
        this.application = application;
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
                            sharedPreferences.edit().putString("user_id",authenticationResponse.getUserId().toString()).apply();
                            Toast.makeText(application, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                            getUserDetail(authenticationResponse.getUserId(), "Bearer " + authenticationResponse.getAccessToken());
                        } else {
                            Toast.makeText(application, "Đăng nhập thất bại !", Toast.LENGTH_SHORT).show();
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

    public void registerUser(User user) {
        Context appContext = application.getApplicationContext();

        userService.register(user).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xử lý đăng ký thành công
                    Intent intent = new Intent(appContext, SignupSuccessActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    appContext.startActivity(intent);

                } else {
                    // Xử lý lỗi
                    switch (response.code()) {
                        case 400:
                            Toast.makeText(appContext, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(appContext, "Số điện thoại trên đã tồn tại", Toast.LENGTH_SHORT).show();
                            break;
                        case 409:
                            Toast.makeText(appContext, "Số điện thoại này đã tồn tại", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(appContext, "Lỗi máy chủ, hãy thử lại sau", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(appContext, "Lỗi không xác định, hãy thử lại sau", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi kết nối
                Toast.makeText(appContext, "Lỗi kết nối", Toast.LENGTH_SHORT).show();

            }
        });
    }
}


