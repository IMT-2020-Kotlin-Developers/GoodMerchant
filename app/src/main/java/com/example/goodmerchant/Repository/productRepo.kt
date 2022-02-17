package com.example.goodmerchant.Repository
import android.app.Application
import android.util.Log
import androidx.navigation.fragment.findNavController


import com.example.goodmerchant.MainFragmentDirections
import com.example.goodmerchant.Retrofit.BASE_URL
import com.example.goodmerchant.Retrofit.productInterface
import com.example.goodmerchant.Retrofit.productModal
import com.example.goodmerchant.Retrofit.serpApiResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
lateinit var productDetailList: Array<productModal>
class productRepo(application: Application) {

    fun getProducts(q : String){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(productInterface::class.java)
        service.getProduct(q).enqueue(object : Callback<serpApiResult> {
            override fun onResponse(call: Call<serpApiResult>, response: Response<serpApiResult>) {

                val productlist: serpApiResult? = response.body()
                if (productlist != null) {
                    productDetailList = productlist.shopping_results
                    Log.d("@@@@", productlist.toString())
                } else{
                    Log.d("@@@","ProductList is null")
                }
            }

            override fun onFailure(call: Call<serpApiResult>, t: Throwable) {
                Log.d("@@@@", "Failed sharam ati hai?", t)
            }
        })

    }
    fun getproductsfromlist(): Array<productModal> {
        return productDetailList
    }

}