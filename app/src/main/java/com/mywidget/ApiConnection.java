package com.mywidget;

import com.mywidget.Interface.LmemoApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConnection {

    private Retrofit retrofit;

    private final String baseUrl = "https://datewidget-ab4ba.firebaseio.com/";

    private ApiConnection() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static final ApiConnection INSTANCE = new ApiConnection();

    public static ApiConnection Instance() { return INSTANCE; }

    public LmemoApi getRetrofitService() {
        return retrofit.create(LmemoApi.class);
    }

}
