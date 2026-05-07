package com.oscar.mintory.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/";
    private static Retrofit retrofit = null;

    public static GoogleBooksApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Convierte el JSON
                    .build();
        }
        return retrofit.create(GoogleBooksApi.class);
    }
}