package com.example.finamarvelapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://akabab.github.io/superhero-api/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofitBuilder.create(ApiService::class.java)
}
