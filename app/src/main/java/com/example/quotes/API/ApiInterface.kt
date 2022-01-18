package com.example.quotes.API

import com.example.quotes.RCV_Classes.QuotesModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("api/quotes")
    fun getData() : Call<List<QuotesModel>>

}