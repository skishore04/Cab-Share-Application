package com.example.mycabshare.Remote;

import android.database.Observable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IGoogleAPI {
    @GET("maps/api/directions/json")
    Observable<String> getDirection(
        @Query("mode") String mode,
                @Query("transit_routing_preference") String transit_routing,
                        @Query("origin") String from,
                                @Query("destination") String to,
                                        @Query("key") String key
    );
//    @GET
//    Call<String> getDataFromGoogleApi(@Url String url);
}
