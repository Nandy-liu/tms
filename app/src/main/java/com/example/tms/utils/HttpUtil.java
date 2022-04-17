package com.example.tms.utils;

import com.example.tms.model.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {
    private static final HttpUtil ourInstance = new HttpUtil();
    private final Retrofit mRetrofit;

    public static HttpUtil getInstance(){
        return ourInstance;
    }

    private HttpUtil(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constans.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Api getApi(){
        Api api = mRetrofit.create(Api.class);
        return api;
    }

    public Retrofit getRetrofit(){
        return mRetrofit;
    }
}
