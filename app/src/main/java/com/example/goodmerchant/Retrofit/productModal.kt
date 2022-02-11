package com.example.goodmerchant.Retrofit

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class productModal(
    val title : String,
    val link : String,
    val price :String,
    val rating : Float,
    val thumbnail : String,
    val source : String,
    val extracted_price : Float,
    val extensions  : Array<String>

): Parcelable
