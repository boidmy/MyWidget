package com.mywidget

import com.mywidget.Interface.LmemoApi
import com.mywidget.data.UserData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiConnection private constructor() {

    private val retrofit: Retrofit

    private val baseUrl = "https://datewidget-ab4ba.firebaseio.com/"

    val retrofitService: LmemoApi get() {
        return retrofit.create(LmemoApi::class.java)
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {

        private val INSTANCE = ApiConnection()

        fun Instance(): ApiConnection {
            return INSTANCE
        }
    }

    fun selectNickName(mail: String): Observable<UserData> =
        retrofitService
            .UserData(mail)
            .subscribeOn(Schedulers.io())
}
