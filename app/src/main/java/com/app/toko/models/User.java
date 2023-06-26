package com.app.toko.models;

import android.util.Patterns;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private String role;

    public User(String firstname, String lastname, String email, String password, String phone, String role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }
}
