package com.example.goodmerchant

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goodmerchant.Retrofit.*
import com.example.goodmerchant.databinding.FragmentMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        binding.camera.setOnClickListener {
            getProducts()
        }
        return binding.root
    }

    private fun getProducts() {
        val products = productService.productInstance.getProduct("mouse")
        products.enqueue(object : Callback<serpApiResult> {
            override fun onResponse(call: Call<serpApiResult>, response: Response<serpApiResult>) {
                val productlist: serpApiResult? = response.body()
                if (productlist != null) {
                    Log.d("@@@@", productlist.toString())
                }
            }

            override fun onFailure(call: Call<serpApiResult>, t: Throwable) {
                Log.d("@@@@", "Failed sharam aatti hau?", t)
            }
        })
    }

}