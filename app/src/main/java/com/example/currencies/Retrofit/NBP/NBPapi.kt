package com.example.currencies.Retrofit.NBP

import retrofit2.http.GET

interface NBPapi {

    @GET("api/exchangerates/tables/A/?format=json")
    suspend fun getNBPrecords(): NBPresponse
}