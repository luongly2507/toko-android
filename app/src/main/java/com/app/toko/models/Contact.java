package com.app.toko.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Contact {
    private String id;
    private String telephone;
    private String receiver;
    private String city;
    private String district;
    private String ward;
    private String line;
    private boolean isDefault;



    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", telephone='" + telephone + '\'' +
                ", receiver='" + receiver + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", ward='" + ward + '\'' +
                ", line='" + line + '\'' +
                '}';
    }
}
