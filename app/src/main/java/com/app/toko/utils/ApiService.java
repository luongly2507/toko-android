package com.app.toko.utils;

import com.app.toko.services.AuthenticationService;
import com.app.toko.services.BookService;
import com.app.toko.services.CategoryService;
import com.app.toko.services.UserService;

public class ApiService {
    private static final String SERVICE_BASE_URL = "http://192.168.1.105:3000/";

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

}
