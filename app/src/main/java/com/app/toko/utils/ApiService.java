package com.app.toko.utils;

import com.app.toko.services.AddressService;
import com.app.toko.services.AuthenticationService;
import com.app.toko.services.BookService;
import com.app.toko.services.CategoryService;
import com.app.toko.services.ContactService;
import com.app.toko.services.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    public static final String SERVICE_BASE_URL = "http://192.168.1.11:3000/";
    public static final String SERVICE_BASE_URL2 = "https://raw.githubusercontent.com/madnh/hanhchinhvn/master/dist/";

    public static CategoryService getCategoryService() {
        return RetrofitClient.getClient(SERVICE_BASE_URL).create(CategoryService.class);
    }
    public static AuthenticationService getAuthenticationService(){
        return RetrofitClient.getClient(SERVICE_BASE_URL).create(AuthenticationService.class);
    }
    public static UserService getUserService(){
        return RetrofitClient.getClient(SERVICE_BASE_URL).create(UserService.class);
    }
    public static BookService getBookService()
    {
        return RetrofitClient.getClient(SERVICE_BASE_URL).create(BookService.class);
    }
    public static ContactService getContactService()
    {
        return RetrofitClient.getClient(SERVICE_BASE_URL).create(ContactService.class);
    }

    public static AddressService getAddressService ()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        AddressService addressService = new Retrofit.Builder()
                .baseUrl(SERVICE_BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(AddressService.class);

        return addressService;
    }
}
