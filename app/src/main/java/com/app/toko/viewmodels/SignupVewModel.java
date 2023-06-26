package com.app.toko.viewmodels;

import android.app.Application;
import android.os.Handler;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.User;
import com.app.toko.payload.request.AuthenticationRequest;
import com.app.toko.repositories.UserRepository;

public class SignupVewModel extends AndroidViewModel {
    public MutableLiveData<String> passwordErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> phoneErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> emailErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> firstnameErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> lastnameErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> genderErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> loginErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> firstname = new MutableLiveData<>();
    public MutableLiveData<String> lastname = new MutableLiveData<>();
    public MutableLiveData<Integer> gender = new MutableLiveData<>();

    private LiveData<User> user;
    private UserRepository userRepository;

    public SignupVewModel(Application application){
        super(application);
        userRepository = new UserRepository(getApplication());
        user = userRepository.getUserLiveData();
    }

    public LiveData<User> getUser(){
        if (user == null) {
            user = new MutableLiveData<>();
        }
        return user;
    }

    public void registerUser() {
        String firstname = this.firstname.getValue();
        String lastname = this.lastname.getValue();
        String email = this.email.getValue();
        String password = this.password.getValue();
        String phone = this.phone.getValue();
        String role = "USER";

        // Kiểm tra thông tin người dùng hợp lệ
        if (!isValidUser(firstname, lastname, email, password, phone)) {
            // Xử lý lỗi thông tin người dùng
            return;
        }

        User newUser = new User(firstname, lastname, email, password, phone, role);
        userRepository.registerUser(newUser);
    }

    private boolean isValidUser(String firstname, String lastname, String email, String password, String phone) {
        // Kiểm tra các thông tin người dùng hợp lệ
        // Có thể kiểm tra độ dài, định dạng email, mật khẩu, số điện thoại, v.v.
        // Nếu thông tin không hợp lệ, gán thông báo lỗi vào các MutableLiveData tương ứng
        // Và trả về false
        // Nếu thông tin hợp lệ, trả về true
        return true;
    }

}
