package com.example.kon_boot.tour;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class PlacesApi {


    private static final String url = "https://maps.googleapis.com/maps/";

    public static RetrofitMaps postService = null;

    public static RetrofitMaps getPostService() {

        if (postService == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

            postService = retrofit.create(RetrofitMaps.class);
        }
        return postService;
    }



    }
