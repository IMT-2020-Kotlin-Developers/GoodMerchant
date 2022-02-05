package com.example.goodmerchant.Retrofit

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
//https://serpapi.com/search.json?engine=google_reverse_image&google_domain=google.com&image_url=https%3A%2F%2Fi.imgur.com%2FHBrB8p0.png&api_key=88e24b82106d51e0d94dd7f31afb6c94c35f0abfaeab6e0ef283deee0f08595e//

    interface tagInterface {
        @GET("search.json?engine=google_reverse_image&google_domain=google.com&api_key=$API_KEY")
        fun getTag(
            @Query("image_url") q: String
        ): Call<imagetagResult>
    }

    object tagservices {
        val tagInstance : tagInterface

        init {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            tagInstance = retrofit.create(tagInterface::class.java)
        }

    }
