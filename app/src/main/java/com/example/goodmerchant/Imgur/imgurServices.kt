package com.example.goodmerchant.Imgur

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
private const val BASE_URL = "https://api.imgur.com"

private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .readTimeout(15, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder().client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface imgurServices {
    @Multipart
    @POST("/3/upload")
    fun postImage(
        @Header("Authorization") clientId: String,
        @Part image: MultipartBody.Part
    ):
            Call<ImgurUploadJson>

}
object ImgurApi {
    val retrofitService: imgurServices by lazy {
        retrofit.create(imgurServices::class.java)
    }
}