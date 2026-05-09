package com.oscar.mintory.data.api;

import com.oscar.mintory.model.api.RawgResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RawgApi {
    @GET("games")
    Call<RawgResponse> searchGames(
            @Query("key") String apiKey,
            @Query("search") String query
    );
}