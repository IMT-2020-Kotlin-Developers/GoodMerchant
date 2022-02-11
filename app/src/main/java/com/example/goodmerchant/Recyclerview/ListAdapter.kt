package com.example.goodmerchant.Recyclerview

import android.app.Activity
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.goodmerchant.R
import com.example.goodmerchant.Retrofit.productModal
import com.squareup.picasso.Picasso
import android.content.Intent
import android.util.Log
import android.widget.AdapterView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment


class ListAdapter(private val productList : Array<productModal>,
                    private val listener: OnItemClickListener
                  ):
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val productImage = itemView.findViewById<ImageView>(R.id.productImageIv)
        val productName = itemView.findViewById<TextView>(R.id.productNameTv)
        val productPrice = itemView.findViewById<TextView>(R.id.productPriceTv)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION)
            listener.onItemClick(position)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)

        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = productList[position]

        Picasso.get().load(currentItem.thumbnail).into(holder.productImage);
        holder.productName.text = currentItem.title
        holder.productPrice.text = currentItem.price

    }

    override fun getItemCount(): Int {
        return productList.size
    }


    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}