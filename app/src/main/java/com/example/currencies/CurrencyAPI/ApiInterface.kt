package com.example.currencies.CurrencyAPI

import com.example.currencies.CurrencyAPI.MyDataItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("tables/A/?format=json")
    fun getData(): Call<List<MyDataItem>>
}