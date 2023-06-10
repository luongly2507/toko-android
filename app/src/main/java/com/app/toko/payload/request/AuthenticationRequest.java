package com.app.toko.payload.request;

import android.util.Patterns;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    private String phone;
    private String password;


    public boolean isPasswordLengthGreaterThan5() {
        if (getPassword() == null) {
            return false;
        }

        return getPassword().length() > 5;
    }
    public boolean isPhoneValid(){
        if (getPhone() == null) {
            return false;
        }
        if (getPhone().length() != 10){
            return false;
        }
        return Patterns.PHONE.matcher(getPhone()).matches();
    }
}
