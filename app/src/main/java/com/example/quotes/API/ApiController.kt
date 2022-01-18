package com.example.quotes.API

import com.example.quotes.RCV_Classes.QuotesModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class ApiController {

    /* var RETROFIT: Retrofit? = null

     fun getApi(): Retrofit? {
         if (RETROFIT == null) {
             RETROFIT = Retrofit.Builder()
                 .baseUrl("https://type.fit/")
 //                .client(OkHttpClient.Builder().build())
                 .addConverterFactory(GsonConverterFactory.create())
                 .build()
         }
         return RETROFIT
     }*/

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://type.fit/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}
