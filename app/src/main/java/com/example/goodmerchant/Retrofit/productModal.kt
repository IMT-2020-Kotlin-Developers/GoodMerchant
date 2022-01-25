package com.example.goodmerchant.Retrofit

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class productModal(
    val title : String,
    val link : String,
    val price :String,
    val rating : String,
    val thumbnail : String,
    val source : String
): Parcelable
