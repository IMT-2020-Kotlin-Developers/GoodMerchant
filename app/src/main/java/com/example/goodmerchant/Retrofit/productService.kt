package com.example.goodmerchant.Retrofit

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://serpapi.com/search.json?engine=google&q=mouse&location=Austin%2C+Texas%2C+United+States&google_domain=google.com&gl=us&hl=en&tbm=shop&api_key=88e24b82106d51e0d94dd7f31afb6c94c35f0abfaeab6e0ef283deee0f08595e
const val BASE_URL = "https://serpapi.com/"
const val API_KEY = "0531aa39c487c08eddf96d9fad966a11df3d78e3bd848a1835175ab0d8d1261c"

interface productInterface {
    @GET("search.json?api_key=$API_KEY&engine=google&location=Delhi%2C+India&google_domain=google.com&gl=in&hl=en&tbm=shop")
    fun getProduct(
        @Query("q") q: String
    ): Call<serpApiResult>
}

object productService {
    val productInstance: productInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        productInstance = retrofit.create(productInterface::class.java)
    }

}