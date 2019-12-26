package com.mywidget.Interface;

import com.google.gson.JsonObject;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LmemoApi {

    String URL = "https://datewidget-ab4ba.firebaseio.com/";

    @GET("Lmemo/{name}.json")
    Call<JsonObject> lmemoData(@Path("name") String name);

    @GET("Lmemo/{name}.json")
    Observable<JsonObject> lmemoData2(@Path("name") String name);
}
