package com.example.kon_boot.tour;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitMaps {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyAKFbBnnGqe0rv6RCnIVFAJZL4uBaoGndc")
    Call<MainPLaces> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}

