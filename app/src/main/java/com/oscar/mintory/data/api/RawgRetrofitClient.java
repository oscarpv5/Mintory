package com.oscar.mintory.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RawgRetrofitClient {
    // La dirección base de la API de videojuegos
    private static final String BASE_URL = "https://api.rawg.io/api/";
    private static Retrofit retrofit = null;

    public static RawgApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RawgApi.class);
    }
}