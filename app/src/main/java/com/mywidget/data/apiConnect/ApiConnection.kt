package com.mywidget.data.apiConnect

import com.mywidget.data.Interface.LmemoApi
import com.mywidget.data.model.UserData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiConnection private constructor() {

    private val retrofit: Retrofit

    private val baseUrl = "https://datewidget-ab4ba.firebaseio.com/"

    val retrofitService: LmemoApi
        get() {
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
            .userData(mail)
            .subscribeOn(Schedulers.io())
}
