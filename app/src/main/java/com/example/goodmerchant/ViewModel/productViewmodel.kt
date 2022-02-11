package com.example.goodmerchant.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.goodmerchant.Repository.productRepo

class productViewmodel(application: Application) : AndroidViewModel(application) {
    val repository  = productRepo(application)
     fun getProducts(q : String){
       repository.getProducts(q)
    }
}