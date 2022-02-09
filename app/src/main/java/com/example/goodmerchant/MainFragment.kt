package com.example.goodmerchant

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.goodmerchant.Recyclerview.ListFragmentDirections
import com.example.goodmerchant.Retrofit.*
import com.example.goodmerchant.ViewModel.productViewmodel
import com.example.goodmerchant.databinding.FragmentMainBinding
import com.google.mlkit.common.model.LocalModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileDescriptor
import java.io.IOException

class MainFragment : Fragment() {


  private lateinit var viewModel : productViewmodel
    lateinit var binding: FragmentMainBinding
    val localModal = LocalModel.Builder().setAssetFilePath("lite-model_object_detection_mobile_object_labeler_v1_1.tflite").build()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(productViewmodel::class.java)
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        //manual search
        var temp : Boolean = false
        binding.searchicon.setOnClickListener {
            if (binding.searchtext.text != null) {
               viewModel.getProducts(binding.searchtext.text.toString())
                Handler().postDelayed({
                    var productDetailList: Array<productModal> = viewModel.repository.getproductsfromlist()
                    val directions = MainFragmentDirections.actionMainFragmentToListFragment(productDetailList)
                    findNavController().navigate(directions)}
                    ,6000)
//                if(temp) {
//                    var productDetailList: Array<productModal> = viewModel.repository.getproductsfromlist()
//                    val directions = MainFragmentDirections.actionMainFragmentToListFragment(productDetailList)
//                    findNavController().navigate(directions)
//                }
//                else{
//                    temp = true
//                }
            }
        }

        binding.camera.setOnClickListener{
//            takeGallery()
            getTags()
        }

        return binding.root
    }


//    fun takeGallery() {
//        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        startActivityForResult(gallery, 100)
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == 100 && requestCode == 100) {
//                var imageUri: Uri? =  data?.data
//            val bitmap = MediaStore.Images.Media.getBitmap(, imageUri)
//        }
//    }
//    private fun getProducts() {
//        val search = binding.searchtext.text.toString()
//        val products = productService.productInstance.getProduct(search)
//        products.enqueue(object : Callback<serpApiResult> {
//            override fun onResponse(call: Call<serpApiResult>, response: Response<serpApiResult>) {
//                val productlist: serpApiResult? = response.body()
//                if (productlist != null) {
//                    val productDetailList: Array<productModal> = productlist.shopping_results
//                    Log.d("@@@@", productlist.toString())
//                    val directions = MainFragmentDirections.actionMainFragmentToListFragment(productDetailList)
//                    findNavController().navigate(directions)
//                }
//            }
//
//            override fun onFailure(call: Call<serpApiResult>, t: Throwable) {
//                Log.d("@@@@", "Failed sharam ati hai?", t)
//            }
//        })
//    }
    fun getTags(){
        val tag = tagservices.tagInstance.getTag("https://rukminim1.flixcart.com/image/880/1056/kpydrbk0/shirt/g/9/p/s-black-5-jai-textiles-original-imag42m5hcfue7np.jpeg?q=50")
        tag.enqueue(object : Callback<imagetagResult>{
                override fun onResponse(call: Call<imagetagResult>, response: Response<imagetagResult>) {
                    val currenttag : imagetagResult? = response.body()
                    if(currenttag != null){
                        val tagDetail : String = currenttag.searchInformation!!.query_displayed

                        Log.d("%%%%%", tagDetail)
                    }
                }

                override fun onFailure(call: Call<imagetagResult>, t: Throwable) {
                    Log.d("%%%%%", "Failed sharam ati hai?", t)
                }
            })
    }


}

