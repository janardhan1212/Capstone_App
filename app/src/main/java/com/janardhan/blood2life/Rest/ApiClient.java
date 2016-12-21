package com.janardhan.blood2life.Rest;


import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class ApiClient {
    //  public static final String BASE_URL = "http://janshs.esy.es /vsb/api/";
    public static final String BASE_URL = "http://egnify.com/";


    public static Retrofit getClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

}
