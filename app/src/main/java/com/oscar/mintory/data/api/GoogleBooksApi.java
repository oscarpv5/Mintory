package com.oscar.mintory.data.api;

import com.oscar.mintory.model.api.GoogleBooksResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleBooksApi {
    // Esto llamará a: https://www.googleapis.com/books/v1/volumes?q=tu_busqueda
    @GET("volumes")
    Call<GoogleBooksResponse> searchBooks(@Query("q") String query);
}