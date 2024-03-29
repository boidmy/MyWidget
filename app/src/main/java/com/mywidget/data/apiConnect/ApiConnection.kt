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

    private val retrofitService: LmemoApi
        get() = retrofit.create(LmemoApi::class.java)

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {

        private val INSTANCE = ApiConnection()

        fun instance(): ApiConnection {
            return INSTANCE
        }
    }
}
