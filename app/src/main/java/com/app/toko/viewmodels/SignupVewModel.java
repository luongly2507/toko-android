package com.app.toko.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.User;
import com.app.toko.repositories.UserRepository;

public class SignupVewModel extends AndroidViewModel {
    public MutableLiveData<String> passwordErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> phoneErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> emailErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> firstnameErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> lastnameErrorMessage = new MutableLiveData<>();
    public MutableLiveData<String> genderErrorMessage = new MutableLiveData<>();
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
            return;
        }

        User newUser = new User(firstname, lastname, email, password, phone, role);
        userRepository.registerUser(newUser);
    }

    private boolean isValidUser(String firstname, String lastname, String email, String password, String phone) {
        boolean isValid = true;

        if (firstname == null || firstname.trim().isEmpty()) {
            firstnameErrorMessage.setValue("Hãy nhập tên của bạn!");
            isValid = false;
        }

        if (lastname == null || lastname.trim().isEmpty()) {
            lastnameErrorMessage.setValue("Hãy nhập họ của bạn!");
            isValid = false;
        }

        if (email == null || email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailErrorMessage.setValue("Hãy nhập địa chỉ email hợp lệ!");
            isValid = false;
        }

        if (password == null || password.trim().isEmpty() || password.length() < 6) {
            passwordErrorMessage.setValue("Mật khẩu phải có ít nhất 6 ký tự!");
            isValid = false;
        }

        if (phone == null
                || phone.trim().isEmpty() || !android.util.Patterns.PHONE.matcher(phone).matches()
                || phone.length() < 10 || phone.length() > 11) {
            phoneErrorMessage.setValue("Vui lòng nhập đúng định dạng số điện thoại!");
            isValid = false;
        }

        return isValid;
    }

}
