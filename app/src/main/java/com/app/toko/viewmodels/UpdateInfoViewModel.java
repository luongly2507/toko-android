package com.app.toko.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.toko.models.User;
import com.app.toko.payload.request.UpdateUserInfoRequest;
import com.app.toko.repositories.UserRepository;

import java.util.Objects;
import java.util.UUID;

public class UpdateInfoViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<User> userLiveData;
    public MutableLiveData<String> lastname , firstname , phone , oldPass , repeatPass , newPass , email;
    public MutableLiveData<String> lastnameError , firstnameError , phoneError , newpassError , oldPassError , repeatPassError , emailError;
    public UpdateInfoViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        userLiveData = userRepository.getUserLiveData();
        firstname = new MutableLiveData<>();
        lastname = new MutableLiveData<>();
        phone = new MutableLiveData<>();
        oldPass = new MutableLiveData<>();
        repeatPass = new MutableLiveData<>();
        newPass = new MutableLiveData<>();
        lastnameError = new MutableLiveData<>();
        firstnameError = new MutableLiveData<>();
        phoneError = new MutableLiveData<>();
        oldPassError = new MutableLiveData<>();
        repeatPassError = new MutableLiveData<>();
        newpassError = new MutableLiveData<>();
        emailError = new MutableLiveData<>();
        email = new MutableLiveData<>();
    }
    public void getUserDetail(UUID userId , String token)
    {
        userRepository.getUserDetail(userId , "Bearer " + token);
    }
    public void updateUserInfo(UUID userId , UpdateUserInfoRequest updateUserInfoRequest , String token)
    {
        userRepository.updateUserInfo(userId , updateUserInfoRequest , "Bearer " + token);
    }
    public void updateUserPassword(String phone , String password)
    {
        userRepository.updateUserPassword(phone , password);
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public boolean isValidUser(Boolean isChangePass) {
        String firstname = this.firstname.getValue();
        String lastname = this.lastname.getValue();
        String email = this.email.getValue();
        String phone = this.phone.getValue();
        boolean isValid = true;

        if (firstname == null || firstname.trim().isEmpty()) {
            firstnameError.setValue("Hãy nhập tên của bạn!");
            isValid = false;
        }

        if (lastname == null || lastname.trim().isEmpty()) {
            lastnameError.setValue("Hãy nhập họ của bạn!");
            isValid = false;
        }

        if (email == null || email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.setValue("Hãy nhập địa chỉ email hợp lệ!");
            isValid = false;
        }

        if (phone == null
                || phone.trim().isEmpty() || !android.util.Patterns.PHONE.matcher(phone).matches()
                || phone.length() < 10 || phone.length() > 11) {
            phoneError.setValue("Số điện thoại phải đúng định dạng và có 10 hoặc 11 kí tự!");
            isValid = false;
        }
        if(isChangePass)
        {
            String newPass = this.newPass.getValue();
            String repeatPass = this.repeatPass.getValue();
            if (newPass == null || newPass.trim().isEmpty() || newPass.length() < 6) {
                newpassError.setValue("Mật khẩu phải có ít nhất 6 ký tự!");
                isValid = false;
            }
            if (!Objects.equals(repeatPass, newPass)) {
                repeatPassError.setValue("Mật khẩu không khớp với mật khẩu mới !");
                isValid = false;
            }
        }
        return isValid;
    }
}
