package com.mywidget.data.Interface

import com.google.gson.JsonObject
import com.mywidget.data.model.UserData
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface LmemoApi {

    @GET("Lmemo/{name}.json")
    fun lmemoData(@Path("name") name: String): Observable<JsonObject>

    @GET("User/{id}.json")
    fun UserData(@Path("id") id: String?): Observable<UserData>

    @POST
    fun fcmTest(@Url url: String, @HeaderMap map: HashMap<String, String>, @Body body: JSONObject): Observable<Response<*>>
}
