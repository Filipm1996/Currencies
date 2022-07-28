package com.example.currencies.data.Retrofit.NBP

import retrofit2.http.GET

interface NBPapi {

    @GET("api/exchangerates/tables/A/?format=json")
    suspend fun getNBPrecords(): NBPresponse
}