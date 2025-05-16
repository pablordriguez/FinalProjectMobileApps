package com.example.finamarvelapp.data.remote

import com.example.finamarvelapp.data.model.Hero
import retrofit2.http.GET

interface ApiService {
    @GET("all.json")
    suspend fun fetchHeroesList(): List<Hero>
}
