package com.example.goodmerchant.Recyclerview

import android.os.Bundle
import android.os.RecoverySystem
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goodmerchant.ProductDetailFragmentArgs
import com.example.goodmerchant.R
import com.example.goodmerchant.Retrofit.productModal
import com.example.goodmerchant.ViewModel.productViewmodel
import com.example.goodmerchant.databinding.FragmentListBinding
import com.example.goodmerchant.databinding.FragmentMainBinding

class ListFragment : Fragment(), ListAdapter.OnItemClickListener{
    lateinit var binding: FragmentListBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mArrayList: Array<productModal>
    private lateinit var viewModel : productViewmodel

    lateinit var titleList : Array<String>
    lateinit var linkList : Array<String>
    lateinit var priceList : Array<String>
    lateinit var ratingList : Array<String>
    lateinit var thumbnailList : Array<String>
    lateinit var sourceList : Array<String>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this).get(productViewmodel::class.java)
//        val bundle = arguments
//        val args = ListFragmentArgs.fromBundle(bundle!!)
//        mArrayList = args.productDetailList
        mArrayList = viewModel.repository.getproductsfromlist()
        mRecyclerView = binding.listRv
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.setHasFixedSize(true)

//        mArrayList = arrayListOf<productModal>()
//        titleList = arrayOf("DELL MS 116 Wired Optical Mouse","HP M100 Wired Optical Gaming Mouse  (USB 2.0, Black)")
//        linkList = arrayOf("https://www.flipkart.com/dell-ms-116-wired-optical-mouse/p/itmekm2ggehfhfaw?pid=ACCEKM2GZAGFW2TM&lid=LSTACCEKM2GZAGFW2TMF3S8CR&marketplace=FLIPKART&q=mouse&store=search.flipkart.com&srno=s_1_1&otracker=search&otracker1=search&fm=SEARCH&iid=58eea59a-74b8-4edc-a41b-b5961ad145f4.ACCEKM2GZAGFW2TM.SEARCH&ppt=hp&ppn=homepage&ssid=0jjew57nsg0000001642703539767&qH=40203abe6e81ed98",
//        "https://www.flipkart.com/hp-m100-wired-optical-gaming-mouse/p/itmf3g2grzzcmpcy?gclid=CjwKCAiA866PBhAYEiwANkIneKN7dzPtgbAqkic3zd_gp4boBquvhl7R1UlRBc5fssqlgDLd1hN0TBoCvwAQAvD_BwE&pid=ACCF3GYQBJRPXS7W&lid=LSTACCF3GYQBJRPXS7WGDUHVS&marketplace=FLIPKART&cmpid=content_mouse_13959857976_g_8965229628_gmc_pla&tgi=sem,1,G,11214002,g,search,,536336469819,,,,c,,,,,,,&ef_id=CjwKCAiA866PBhAYEiwANkIneKN7dzPtgbAqkic3zd_gp4boBquvhl7R1UlRBc5fssqlgDLd1hN0TBoCvwAQAvD_BwE:G:s&s_kwcid=AL!739!3!536336469819!!!g!911885940614!&gclsrc=aw.ds")
//        priceList = arrayOf("269","579")
//        ratingList = arrayOf("5.0","4.5")
//        thumbnailList = arrayOf("https://rukminim1.flixcart.com/image/416/416/k4a7c7k0/mouse/m/h/t/dell-ms116-wired-optical-mouse-3yrs-warranty-original-imafmktgxyt5ge9k.jpeg?q=70",
//        "https://rukminim1.flixcart.com/image/416/416/ji20r680/mouse/s/7/w/hp-m100-original-imaf5xmxcgwzydzn.jpeg?q=70")
//        sourceList = arrayOf("Amazon","Flipkart")

        getUserData()
//        findNavController().navigate(R.id.action_listFragment_to_productDetailFragment)
        return binding.root
    }

    private fun getUserData() {
//        for(i in titleList.indices){
//            val item = productModal(titleList[i],linkList[i],priceList[i],ratingList[i],thumbnailList[i],sourceList[i])
//            mArrayList.add(item)
//        }
        mArrayList.sortBy { it.extracted_price }
        var adapter = ListAdapter(mArrayList,this)
        mRecyclerView.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        Log.d("@","Item $position clicked")
        val directions = ListFragmentDirections.actionListFragmentToProductDetailFragment(mArrayList[position])
        findNavController().navigate(directions)
    }
}